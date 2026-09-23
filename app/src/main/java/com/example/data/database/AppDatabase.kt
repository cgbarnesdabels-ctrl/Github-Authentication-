package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.HealthMetric
import com.example.data.model.SyncEvent
import com.example.data.model.UserProfile

@Database(entities = [SyncEvent::class, HealthMetric::class, UserProfile::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun syncEventDao(): SyncEventDao
    abstract fun healthMetricDao(): HealthMetricDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "health_sync_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
