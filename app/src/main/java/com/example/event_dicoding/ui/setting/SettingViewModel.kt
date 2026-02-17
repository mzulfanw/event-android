package com.example.event_dicoding.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.event_dicoding.data.local.SettingPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SettingViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSettings(): Flow<Boolean> = pref.getThemeSetting()

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getReminderSettings(): Flow<Boolean> = pref.getReminderSetting()

    fun saveReminderSetting(isReminderActive: Boolean) {
        viewModelScope.launch {
            pref.saveReminderSetting(isReminderActive)
        }
    }
}
