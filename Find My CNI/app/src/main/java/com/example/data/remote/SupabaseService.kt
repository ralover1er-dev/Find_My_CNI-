package com.example.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class SupabaseService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val dtoAdapter = moshi.adapter(SupabaseCniDto::class.java)
    private val listAdapter = moshi.adapter<List<SupabaseCniDto>>(
        Types.newParameterizedType(List::class.java, SupabaseCniDto::class.java)
    )

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun fetchDeclarations(): Result<List<SupabaseCniDto>> = withContext(Dispatchers.IO) {
        try {
            val url = "${SupabaseConfig.restEndpoint}?select=*&order=timestamp.desc"
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", SupabaseConfig.supabaseAnonKey)
                .addHeader("Authorization", "Bearer ${SupabaseConfig.supabaseAnonKey}")
                .addHeader("Accept", "application/json")
                .get()
                .build()

            val response = client.newCall(request).execute()
            val bodyString = response.body?.string()

            if (response.isSuccessful && !bodyString.isNullOrBlank()) {
                val list = listAdapter.fromJson(bodyString) ?: emptyList()
                Result.success(list)
            } else {
                Result.failure(Exception("Supabase HTTP ${response.code}: ${response.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun insertDeclaration(dto: SupabaseCniDto): Result<SupabaseCniDto> = withContext(Dispatchers.IO) {
        try {
            val json = dtoAdapter.toJson(dto)
            val requestBody = json.toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url(SupabaseConfig.restEndpoint)
                .addHeader("apikey", SupabaseConfig.supabaseAnonKey)
                .addHeader("Authorization", "Bearer ${SupabaseConfig.supabaseAnonKey}")
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=representation")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val bodyString = response.body?.string()

            if (response.isSuccessful && !bodyString.isNullOrBlank()) {
                // PostgREST returns an array of inserted rows if return=representation
                val list = listAdapter.fromJson(bodyString)
                val inserted = list?.firstOrNull() ?: dto
                Result.success(inserted)
            } else {
                Result.failure(Exception("Supabase HTTP ${response.code}: $bodyString"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateStatus(id: Long, status: String, isVerified: Boolean): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val patchJson = """
                {
                  "status": "$status",
                  "is_verified_by_authority": $isVerified
                }
            """.trimIndent()

            val url = "${SupabaseConfig.restEndpoint}?id=eq.$id"
            val requestBody = patchJson.toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", SupabaseConfig.supabaseAnonKey)
                .addHeader("Authorization", "Bearer ${SupabaseConfig.supabaseAnonKey}")
                .addHeader("Content-Type", "application/json")
                .patch(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Supabase HTTP ${response.code}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun testConnection(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val url = "${SupabaseConfig.supabaseUrl.trimEnd('/')}/rest/v1/"
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", SupabaseConfig.supabaseAnonKey)
                .addHeader("Authorization", "Bearer ${SupabaseConfig.supabaseAnonKey}")
                .get()
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful || response.code == 200 || response.code == 404) {
                Result.success("Serveur Supabase Cloud joignable (${response.code} OK)")
            } else {
                Result.failure(Exception("HTTP Code ${response.code}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
