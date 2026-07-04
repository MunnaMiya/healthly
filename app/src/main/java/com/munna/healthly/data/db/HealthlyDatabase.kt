package com.munna.healthly.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [ExerciseEntity::class, UserProfileEntity::class], version = 1, exportSchema = false)
abstract class HealthlyDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile private var INSTANCE: HealthlyDatabase? = null

        fun getDatabase(context: Context): HealthlyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthlyDatabase::class.java,
                    "healthly_db"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Pre-populate exercises from JSON asset
                        CoroutineScope(kotlinx.coroutines.Dispatchers.Default).launch {
                            // Note: Actual population happens in RepositoryImpl initializeDatabaseIfNeeded
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
