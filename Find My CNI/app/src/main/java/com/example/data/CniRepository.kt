package com.example.data

import com.example.data.remote.SupabaseCniDto
import com.example.data.remote.SupabaseService
import kotlinx.coroutines.flow.Flow

class CniRepository(
    private val dao: CniDao,
    private val supabaseService: SupabaseService = SupabaseService()
) {

    val allEntries: Flow<List<CniEntity>> = dao.getAllEntries()

    fun search(query: String): Flow<List<CniEntity>> {
        return if (query.isBlank()) {
            dao.getAllEntries()
        } else {
            dao.searchEntries(query.trim())
        }
    }

    suspend fun getById(id: Long): CniEntity? = dao.getEntryById(id)

    suspend fun insert(entry: CniEntity): Long {
        val insertedId = dao.insertEntry(entry)
        val insertedEntity = entry.copy(id = insertedId)

        // Asynchronously or inline push to Supabase Cloud Database
        try {
            val dto = SupabaseCniDto.fromEntity(insertedEntity)
            val result = supabaseService.insertDeclaration(dto)
            if (result.isSuccess) {
                val cloudDto = result.getOrNull()
                if (cloudDto?.id != null && cloudDto.id != insertedId) {
                    // Update local entity with remote ID if needed
                }
            }
        } catch (_: Exception) {
            // Graceful fallback to local Room DB if network fails
        }

        return insertedId
    }

    suspend fun update(entry: CniEntity) {
        dao.updateEntry(entry)

        try {
            supabaseService.updateStatus(
                id = entry.id,
                status = entry.status,
                isVerified = entry.isVerifiedByAuthority
            )
        } catch (_: Exception) {
            // Local update succeeded
        }
    }

    suspend fun delete(id: Long) = dao.deleteEntryById(id)

    suspend fun syncWithSupabaseServer(): Result<Int> {
        return try {
            val remoteResult = supabaseService.fetchDeclarations()
            if (remoteResult.isSuccess) {
                val remoteList = remoteResult.getOrDefault(emptyList())
                if (remoteList.isNotEmpty()) {
                    val entities = remoteList.map { it.toEntity() }
                    dao.insertAll(entities)
                }
                Result.success(remoteList.size)
            } else {
                // If remote fetch failed (e.g. initial setup without live API key), sync local entries
                val localEntries = dao.getAllEntriesList()
                for (local in localEntries) {
                    supabaseService.insertDeclaration(SupabaseCniDto.fromEntity(local))
                }
                Result.success(localEntries.size)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun testSupabaseConnection(): Result<String> {
        return supabaseService.testConnection()
    }

    suspend fun seedInitialDataIfEmpty() {
        if (dao.getCount() == 0) {
            val initialEntries = listOf(
                CniEntity(
                    entryType = "FOUND",
                    fullName = "ZEBOULOUMO GUY CYRILLE",
                    dob = "12/08/1988",
                    last4Digits = "4821",
                    location = "Douala, Marché Central",
                    description = "CNI retrouvée près du stand de textile au Marché Central. Remise au poste de gardiennage.",
                    contactPhone = "+237 699 88 77 66",
                    status = "TROUVÉE",
                    isVerifiedByAuthority = true,
                    userEmail = "leslyzoyem297@gmail.com",
                    createdAtFormatted = "Il y a 2h"
                ),
                CniEntity(
                    entryType = "FOUND",
                    fullName = "NGONO MARIE CLAIRE",
                    dob = "03/04/1995",
                    last4Digits = "1097",
                    location = "Yaoundé, Mvog-Mbi",
                    description = "Carte oubliée sur un comptoir de boutique à Mvog-Mbi, Yaoundé.",
                    contactPhone = "+237 677 11 22 33",
                    status = "VÉRIFIÉ",
                    isVerifiedByAuthority = true,
                    userEmail = "leslyzoyem297@gmail.com",
                    createdAtFormatted = "Il y a 5h"
                ),
                CniEntity(
                    entryType = "FOUND",
                    fullName = "MBALLA PAUL ERIC",
                    dob = "25/11/1990",
                    last4Digits = "3340",
                    location = "Douala, Gare Voyageurs",
                    description = "Trouvé par terre devant la billetterie de la gare ferroviaire.",
                    contactPhone = "+237 690 44 55 66",
                    status = "TROUVÉE",
                    isVerifiedByAuthority = true,
                    userEmail = "leslyzoyem297@gmail.com",
                    createdAtFormatted = "Il y a 1 jour"
                ),
                CniEntity(
                    entryType = "LOST",
                    fullName = "KAMGA TCHINDA BERTRAND",
                    dob = "18/02/1993",
                    last4Digits = "5821",
                    location = "Yaoundé, Marché Mokolo",
                    description = "Portefeuille tombé lors d'un trajet en taxi vers Mokolo.",
                    contactPhone = "+237 655 00 11 22",
                    status = "EN_ATTENTE_VALIDATION",
                    isVerifiedByAuthority = false,
                    userEmail = "leslyzoyem297@gmail.com",
                    createdAtFormatted = "Il y a 30 min"
                )
            )
            dao.insertAll(initialEntries)
        }
    }
}
