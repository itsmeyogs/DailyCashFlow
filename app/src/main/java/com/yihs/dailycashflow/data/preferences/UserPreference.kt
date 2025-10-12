package com.yihs.dailycashflow.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yihs.dailycashflow.data.model.LoginResponse
import com.yihs.dailycashflow.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference (private val context: Context){
    companion object{
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val EMAIL_KEY = stringPreferencesKey("email_user")
        private val NAME_KEY = stringPreferencesKey("name_user")
    }

    suspend fun saveSession(data: LoginResponse){
        context.dataStore.edit {
            it[TOKEN_KEY] = data.token
            it[EMAIL_KEY] = data.user.email
            it[NAME_KEY] = data.user.name
        }
    }

    fun getSession(): Flow<User>{
        return context.dataStore.data.map {
            User(
                it[NAME_KEY] ?: "",
                it[EMAIL_KEY] ?: "",
                it[TOKEN_KEY] ?: ""
            )
        }
    }

    suspend fun removeSession(){
        context.dataStore.edit { it.clear() }
    }
}