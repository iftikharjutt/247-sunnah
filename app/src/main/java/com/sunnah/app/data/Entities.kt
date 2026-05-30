package com.sunnah.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // e.g., "Sunrise", "Daytime", "Sunset", "Sleep"
    val arabicText: String,
    val translation: String,
    val reference: String, // e.g., "Sahih al-Bukhari"
    val baseHour: Int
)

@Entity(tableName = "routine_completions", primaryKeys = ["date", "routineId"])
data class RoutineCompletion(
    val date: String, // YYYY-MM-DD
    val routineId: Int
)

@Entity(tableName = "dhikr_records")
data class DhikrRecord(
    @PrimaryKey val id: String, // e.g., "Subhan'Allah_2026-05-30"
    val phrase: String,
    val date: String,
    val count: Int,
    val target: Int
)

@Entity(tableName = "notification_settings")
data class NotificationSetting(
    @PrimaryKey val type: String, // e.g., "Tahajjud", "Sunrise"
    val isEnabled: Boolean,
    val hour: Int,
    val minute: Int
)

@Entity(tableName = "app_config")
data class AppConfig(
    @PrimaryKey val key: String,
    val value: String
)
