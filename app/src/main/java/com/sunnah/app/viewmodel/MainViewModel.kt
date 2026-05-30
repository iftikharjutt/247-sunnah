package com.sunnah.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sunnah.app.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SunnahRepository
    val todayDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    init {
        val database = SunnahDatabase.getDatabase(application)
        repository = SunnahRepository(database.sunnahDao())
        seedDatabase()
    }

    val allRoutines = repository.getAllRoutines()
    val completions = repository.getCompletionsForDate(todayDate)
    val dhikrRecords = repository.getDhikrRecordsForDate(todayDate)
    val settings = repository.getNotificationSettings()
    val apiKey = repository.getApiKey()

    fun toggleCompletion(routineId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleCompletion(todayDate, routineId, isCompleted)
        }
    }

    fun saveApiKey(key: String) {
        viewModelScope.launch {
            repository.saveApiKey(key)
        }
    }

    fun updateDhikrCount(phrase: String, count: Int, target: Int) {
        viewModelScope.launch {
            repository.updateDhikrCount(phrase, todayDate, count, target)
        }
    }

    fun updateSetting(setting: NotificationSetting) {
        viewModelScope.launch {
            repository.updateNotificationSetting(setting)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    private fun seedDatabase() {
        viewModelScope.launch {
            // Seed some initial routines if empty
            allRoutines.first().let {
                if (it.isEmpty()) {
                    repository.insertRoutine(Routine(title = "Morning Adhkar", category = "Sunrise", arabicText = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ", translation = "We have entered the morning and at this very time the whole kingdom belongs to Allah", reference = "Sahih Muslim", baseHour = 6))
                    repository.insertRoutine(Routine(title = "Siwak", category = "All", arabicText = "السواك مطهرة للفم مرضاة للرب", translation = "The Siwak is a means of purifying the mouth and pleasing the Lord", reference = "Sahih al-Bukhari", baseHour = 8))
                    repository.insertRoutine(Routine(title = "Midday Nap (Qailulah)", category = "Daytime", arabicText = "قيلوا فإن الشياطين لا تقيل", translation = "Take a midday nap, for the shayaateen do not take a midday nap", reference = "Sahih al-Jami", baseHour = 13))
                }
            }
        }
    }
}
