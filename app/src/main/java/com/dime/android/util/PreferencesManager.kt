package com.dime.android.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dime_preferences")

@Singleton
class PreferencesManager @Inject constructor(private val context: Context) {
    
    companion object {
        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val SHOW_CENTS = booleanPreferencesKey("show_cents")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
        val HAPTIC_FEEDBACK = booleanPreferencesKey("haptic_feedback")
        val CURRENCY = stringPreferencesKey("currency")
        val LOG_TIME_FRAME = intPreferencesKey("log_time_frame")
    }

    val isFirstLaunch: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[FIRST_LAUNCH] ?: true
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DARK_MODE] ?: false
    }

    val showCents: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[SHOW_CENTS] ?: true
    }

    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_ENABLED] ?: true
    }

    val biometricEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[BIOMETRIC_ENABLED] ?: false
    }

    val hapticFeedback: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[HAPTIC_FEEDBACK] ?: true
    }

    val currency: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CURRENCY] ?: "USD"
    }

    suspend fun setFirstLaunch(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[FIRST_LAUNCH] = value
        }
    }

    suspend fun setDarkMode(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE] = value
        }
    }

    suspend fun setShowCents(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SHOW_CENTS] = value
        }
    }

    suspend fun setNotificationsEnabled(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED] = value
        }
    }

    suspend fun setBiometricEnabled(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[BIOMETRIC_ENABLED] = value
        }
    }

    suspend fun setHapticFeedback(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[HAPTIC_FEEDBACK] = value
        }
    }

    suspend fun setCurrency(value: String) {
        context.dataStore.edit { preferences ->
            preferences[CURRENCY] = value
        }
    }
}
