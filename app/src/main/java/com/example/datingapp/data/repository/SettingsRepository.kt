package com.example.datingapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        private val THEME_KEY = booleanPreferencesKey("is_dark_theme")
        private val IT_MODE_KEY = booleanPreferencesKey("is_it_mode")
        private val LANGUAGE_KEY = stringPreferencesKey("language_code")
    }

    // Theme: true = Dark, false = Light
    // Default to system settings if possible, but here default false (Light) as per prompt style
    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: false
        }

    val languageCode: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[LANGUAGE_KEY] ?: "ru" // Default to Russian as per context/names
        }

    val isItMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IT_MODE_KEY] ?: false
        }

    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDark
        }
    }

    suspend fun setLanguage(code: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = code
        }
    }

    suspend fun setItMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IT_MODE_KEY] = enabled
        }
    }
}
