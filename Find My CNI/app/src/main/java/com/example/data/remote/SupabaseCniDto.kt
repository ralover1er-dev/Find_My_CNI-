package com.example.data.remote

import com.example.data.CniEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SupabaseCniDto(
    @Json(name = "id") val id: Long? = null,
    @Json(name = "entry_type") val entryType: String,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "dob") val dob: String,
    @Json(name = "last_4_digits") val last4Digits: String,
    @Json(name = "location") val location: String,
    @Json(name = "declaration_doc_uri") val declarationDocUri: String? = null,
    @Json(name = "selfie_uri") val selfieUri: String? = null,
    @Json(name = "photo_uri") val photoUri: String? = null,
    @Json(name = "description") val description: String? = null,
    @Json(name = "contact_phone") val contactPhone: String = "+237 690 12 34 56",
    @Json(name = "status") val status: String = "TROUVÉE",
    @Json(name = "is_verified_by_authority") val isVerifiedByAuthority: Boolean = false,
    @Json(name = "user_email") val userEmail: String = "leslyzoyem297@gmail.com",
    @Json(name = "created_at_formatted") val createdAtFormatted: String = "Il y a quelques instants",
    @Json(name = "timestamp") val timestamp: Long = System.currentTimeMillis()
) {
    fun toEntity(): CniEntity {
        return CniEntity(
            id = id ?: 0,
            entryType = entryType,
            fullName = fullName,
            dob = dob,
            last4Digits = last4Digits,
            location = location,
            declarationDocUri = declarationDocUri,
            selfieUri = selfieUri,
            photoUri = photoUri,
            description = description,
            contactPhone = contactPhone,
            status = status,
            isVerifiedByAuthority = isVerifiedByAuthority,
            userEmail = userEmail,
            createdAtFormatted = createdAtFormatted,
            timestamp = timestamp
        )
    }

    companion object {
        fun fromEntity(entity: CniEntity): SupabaseCniDto {
            return SupabaseCniDto(
                id = if (entity.id > 0) entity.id else null,
                entryType = entity.entryType,
                fullName = entity.fullName,
                dob = entity.dob,
                last4Digits = entity.last4Digits,
                location = entity.location,
                declarationDocUri = entity.declarationDocUri,
                selfieUri = entity.selfieUri,
                photoUri = entity.photoUri,
                description = entity.description,
                contactPhone = entity.contactPhone,
                status = entity.status,
                isVerifiedByAuthority = entity.isVerifiedByAuthority,
                userEmail = entity.userEmail,
                createdAtFormatted = entity.createdAtFormatted,
                timestamp = entity.timestamp
            )
        }
    }
}
