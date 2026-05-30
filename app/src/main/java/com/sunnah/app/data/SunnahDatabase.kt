package com.sunnah.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Routine::class, RoutineCompletion::class, DhikrRecord::class, NotificationSetting::class, AppConfig::class],
    version = 1,
    exportSchema = false
)
abstract class SunnahDatabase : RoomDatabase() {
    abstract fun sunnahDao(): SunnahDao

    companion object {
        @Volatile
        private var Instance: SunnahDatabase? = null

        fun getDatabase(context: Context): SunnahDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, SunnahDatabase::class.java, "sunnah_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
