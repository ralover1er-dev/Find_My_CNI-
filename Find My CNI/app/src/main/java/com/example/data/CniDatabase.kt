package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CniEntity::class], version = 1, exportSchema = false)
abstract class CniDatabase : RoomDatabase() {

    abstract fun cniDao(): CniDao

    companion object {
        @Volatile
        private var INSTANCE: CniDatabase? = null

        fun getDatabase(context: Context): CniDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CniDatabase::class.java,
                    "find_my_cni_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
