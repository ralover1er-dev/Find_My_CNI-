package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CniDao {

    @Query("SELECT * FROM cni_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<CniEntity>>

    @Query("SELECT * FROM cni_entries ORDER BY timestamp DESC")
    suspend fun getAllEntriesList(): List<CniEntity>

    @Query("SELECT * FROM cni_entries WHERE entryType = :type ORDER BY timestamp DESC")
    fun getEntriesByType(type: String): Flow<List<CniEntity>>

    @Query("SELECT * FROM cni_entries WHERE id = :id")
    suspend fun getEntryById(id: Long): CniEntity?

    @Query("""
        SELECT * FROM cni_entries 
        WHERE LOWER(fullName) LIKE '%' || LOWER(:query) || '%' 
           OR last4Digits LIKE '%' || :query || '%'
           OR dob LIKE '%' || :query || '%'
        ORDER BY timestamp DESC
    """)
    fun searchEntries(query: String): Flow<List<CniEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: CniEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<CniEntity>)

    @Update
    suspend fun updateEntry(entry: CniEntity)

    @Query("DELETE FROM cni_entries WHERE id = :id")
    suspend fun deleteEntryById(id: Long)

    @Query("SELECT COUNT(*) FROM cni_entries")
    suspend fun getCount(): Int
}
