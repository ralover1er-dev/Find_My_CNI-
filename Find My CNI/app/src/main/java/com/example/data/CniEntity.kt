package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cni_entries")
data class CniEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val entryType: String, // "LOST" (J'ai perdu) or "FOUND" (J'ai trouvé)
    val fullName: String,
    val dob: String, // e.g. "12/08/1988" or "JJ/MM/AAAA"
    val last4Digits: String, // e.g. "4821"
    val location: String, // e.g. "Douala, Marché Central"
    val declarationDocUri: String? = null,
    val selfieUri: String? = null,
    val photoUri: String? = null,
    val description: String? = null,
    val contactPhone: String = "+237 690 12 34 56",
    val status: String = "TROUVÉE", // "TROUVÉE", "VÉRIFIÉ", "EN_ATTENTE_VALIDATION", "APPROUVÉE", "REJETÉE"
    val isVerifiedByAuthority: Boolean = false,
    val userEmail: String = "leslyzoyem297@gmail.com",
    val createdAtFormatted: String = "Il y a 2h",
    val timestamp: Long = System.currentTimeMillis()
)
