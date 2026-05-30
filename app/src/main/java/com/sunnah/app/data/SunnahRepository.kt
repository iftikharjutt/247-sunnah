package com.sunnah.app.data

import kotlinx.coroutines.flow.Flow

class SunnahRepository(private val sunnahDao: SunnahDao) {
    fun getAllRoutines(): Flow<List<Routine>> = sunnahDao.getAllRoutines()
    
    fun getCompletionsForDate(date: String): Flow<List<RoutineCompletion>> = 
        sunnahDao.getCompletionsForDate(date)

    suspend fun toggleCompletion(date: String, routineId: Int, isCompleted: Boolean) {
        if (isCompleted) {
            sunnahDao.insertCompletion(RoutineCompletion(date, routineId))
        } else {
            sunnahDao.deleteCompletion(RoutineCompletion(date, routineId))
        }
    }

    fun getDhikrRecordsForDate(date: String): Flow<List<DhikrRecord>> = 
        sunnahDao.getDhikrRecordsForDate(date)

    suspend fun updateDhikrCount(phrase: String, date: String, count: Int, target: Int) {
        sunnahDao.insertDhikrRecord(DhikrRecord("${phrase}_$date", phrase, date, count, target))
    }

    fun getNotificationSettings(): Flow<List<NotificationSetting>> = 
        sunnahDao.getNotificationSettings()

    suspend fun updateNotificationSetting(setting: NotificationSetting) {
        sunnahDao.insertNotificationSetting(setting)
    }

    suspend fun clearHistory() = sunnahDao.clearHistory()
    
    suspend fun insertRoutine(routine: Routine) = sunnahDao.insertRoutine(routine)

    // API Key management
    fun getApiKey(): Flow<String?> = sunnahDao.getConfigValue("GEMINI_API_KEY")

    suspend fun saveApiKey(key: String) {
        sunnahDao.insertConfig(AppConfig("GEMINI_API_KEY", key))
    }
}
