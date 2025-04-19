package com.capstone.warungpintar.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_preferences")

class UserPreferences(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val USER_LOGGED_IN_KEY = booleanPreferencesKey("user_logged_in")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    }

    fun getUserEmail(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USER_EMAIL_KEY] ?: ""
        }
    }

    fun setUserEmail(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences[USER_EMAIL_KEY] = email
            }
        }
    }

    fun isUserLoggedIn(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[USER_LOGGED_IN_KEY] == true
        }
    }

    fun setUserLoggedIn(isLoggedIn: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences[USER_LOGGED_IN_KEY] = isLoggedIn
            }
        }
    }
}