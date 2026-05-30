package com.sunnah.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SunnahDao {
    // Routines
    @Query("SELECT * FROM routines")
    fun getAllRoutines(): Flow<List<Routine>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: Routine)

    // Completions
    @Query("SELECT * FROM routine_completions WHERE date = :date")
    fun getCompletionsForDate(date: String): Flow<List<RoutineCompletion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletion(completion: RoutineCompletion)

    @Delete
    suspend fun deleteCompletion(completion: RoutineCompletion)

    // Dhikr
    @Query("SELECT * FROM dhikr_records WHERE date = :date")
    fun getDhikrRecordsForDate(date: String): Flow<List<DhikrRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDhikrRecord(record: DhikrRecord)

    // Settings
    @Query("SELECT * FROM notification_settings")
    fun getNotificationSettings(): Flow<List<NotificationSetting>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotificationSetting(setting: NotificationSetting)

    @Query("DELETE FROM routine_completions")
    suspend fun clearHistory()

    // App Config
    @Query("SELECT value FROM app_config WHERE `key` = :key")
    fun getConfigValue(key: String): Flow<String?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: AppConfig)
}
