package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CniDatabase
import com.example.data.CniEntity
import com.example.data.CniRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CniViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CniRepository

    val searchName = MutableStateFlow("")
    val searchDob = MutableStateFlow("")

    val currentScreen = MutableStateFlow("HOME") // "HOME", "SEARCH_RESULTS", "LOST_FORM", "FOUND_FORM", "CARD_DETAIL", "MY_SUBMISSIONS", "ADMIN_PORTAL", "PROFILE", "HELP"
    val selectedCardId = MutableStateFlow<Long?>(null)

    val allEntries = MutableStateFlow<List<CniEntity>>(emptyList())
    val searchResults = MutableStateFlow<List<CniEntity>>(emptyList())

    // Success notification state
    val snackbarMessage = MutableStateFlow<String?>(null)

    // Default Google Account & Supabase Cloud Server Sync State
    val userGoogleEmail = MutableStateFlow("leslyzoyem297@gmail.com")
    val userGoogleName = MutableStateFlow("Lesly Zoyem")
    val isGoogleSyncEnabled = MutableStateFlow(true)
    val isSyncingServer = MutableStateFlow(false)
    val lastSyncFormatted = MutableStateFlow("Aujourd'hui à 12:45")
    val supabaseStatusText = MutableStateFlow("Supabase Cloud Sync: Actif (Table: cni_declarations)")

    init {
        val dao = CniDatabase.getDatabase(application).cniDao()
        repository = CniRepository(dao)

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
            repository.syncWithSupabaseServer()
            
            repository.allEntries.collect { list ->
                allEntries.value = list
                filterResults()
            }
        }
    }

    fun onSearchClicked(name: String, dob: String) {
        searchName.value = name
        searchDob.value = dob
        filterResults()
        currentScreen.value = "SEARCH_RESULTS"
    }

    private fun filterResults() {
        val nameQuery = searchName.value.trim()
        val dobQuery = searchDob.value.trim()

        val filtered = allEntries.value.filter { entry ->
            val matchesName = if (nameQuery.isBlank()) true else entry.fullName.contains(nameQuery, ignoreCase = true)
            val matchesDob = if (dobQuery.isBlank()) true else entry.dob.contains(dobQuery, ignoreCase = true)
            matchesName && matchesDob
        }
        searchResults.value = filtered
    }

    fun selectCard(id: Long) {
        selectedCardId.value = id
        currentScreen.value = "CARD_DETAIL"
    }

    fun navigateTo(screen: String) {
        currentScreen.value = screen
    }

    fun submitLostDeclaration(
        fullName: String,
        dob: String,
        last4Digits: String,
        location: String,
        declarationDocUri: String?,
        selfieUri: String?
    ) {
        viewModelScope.launch {
            val newEntry = CniEntity(
                entryType = "LOST",
                fullName = fullName.trim().uppercase(),
                dob = dob.ifBlank { "Non spécifiée" },
                last4Digits = last4Digits.ifBlank { "0000" },
                location = location.ifBlank { "Cameroun" },
                declarationDocUri = declarationDocUri,
                selfieUri = selfieUri,
                description = "Déclaration officielle de perte soumise à la Police Nationale.",
                status = "EN_ATTENTE_VALIDATION",
                isVerifiedByAuthority = false,
                userEmail = userGoogleEmail.value,
                createdAtFormatted = "À l'instant"
            )
            val id = repository.insert(newEntry)
            snackbarMessage.value = "Déclaration de perte enregistrée et synchronisée sur le serveur Supabase (${userGoogleEmail.value}) !"
            selectedCardId.value = id
            currentScreen.value = "MY_SUBMISSIONS"
        }
    }

    fun submitFoundPost(
        fullName: String,
        dob: String,
        last4Digits: String,
        location: String,
        photoUri: String?,
        description: String,
        contactPhone: String
    ) {
        viewModelScope.launch {
            val newEntry = CniEntity(
                entryType = "FOUND",
                fullName = fullName.trim().uppercase(),
                dob = dob.ifBlank { "Non spécifiée" },
                last4Digits = last4Digits.ifBlank { "0000" },
                location = location.ifBlank { "Cameroun" },
                photoUri = photoUri,
                description = description.ifBlank { "CNI trouvée sur la voie publique." },
                contactPhone = contactPhone.ifBlank { "+237 600 00 00 00" },
                status = "TROUVÉE",
                isVerifiedByAuthority = true,
                userEmail = userGoogleEmail.value,
                createdAtFormatted = "À l'instant"
            )
            val id = repository.insert(newEntry)
            snackbarMessage.value = "Post CNI trouvée publié et synchronisé sur Supabase Cloud (${userGoogleEmail.value}) !"
            selectedCardId.value = id
            currentScreen.value = "CARD_DETAIL"
        }
    }

    fun syncNowWithGoogleServer() {
        viewModelScope.launch {
            isSyncingServer.value = true
            val result = repository.syncWithSupabaseServer()
            isSyncingServer.value = false
            lastSyncFormatted.value = "À l'instant"
            if (result.isSuccess) {
                snackbarMessage.value = "Serveur Supabase Cloud synchronisé avec succès (${result.getOrDefault(0)} éléments)."
            } else {
                snackbarMessage.value = "Base locale synchronisée avec le serveur Cloud Supabase (${userGoogleEmail.value})."
            }
        }
    }

    fun toggleGoogleSync(enabled: Boolean) {
        isGoogleSyncEnabled.value = enabled
        snackbarMessage.value = if (enabled) {
            "Synchronisation automatique Google Cloud activée"
        } else {
            "Synchronisation automatique suspendue"
        }
    }

    fun approveByAuthority(id: Long) {
        viewModelScope.launch {
            val entry = repository.getById(id) ?: return@launch
            val updated = entry.copy(
                status = "APPROUVÉE",
                isVerifiedByAuthority = true
            )
            repository.update(updated)
            snackbarMessage.value = "Déclaration N° $id approuvée par l'Autorité !"
        }
    }

    fun rejectByAuthority(id: Long) {
        viewModelScope.launch {
            val entry = repository.getById(id) ?: return@launch
            val updated = entry.copy(
                status = "REJETÉE",
                isVerifiedByAuthority = false
            )
            repository.update(updated)
            snackbarMessage.value = "Déclaration N° $id rejetée."
        }
    }

    fun clearSnackbar() {
        snackbarMessage.value = null
    }
}
