package com.yihs.dailycashflow.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference (private val context: Context){
    companion object{
        private val TOKEN = stringPreferencesKey("token")
    }

    suspend fun saveToken(token: String){
        context.dataStore.edit {
            it[TOKEN] = token
        }
    }

    suspend fun getToken():String?{
        val prefs = context.dataStore.data.map { it[TOKEN]}
        return prefs.first()
    }

    suspend fun removeToken(){
        context.dataStore.edit { it.clear() }
    }

}