package dev.ivanravasi.piggy.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


class TokenRepository(private val context: Context) {
    private companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        val TOKEN_KEY = stringPreferencesKey("token")
        val DOMAIN_KEY = stringPreferencesKey("domain")
    }

    suspend fun saveAuthData(token: String, domain: String) {
        context.dataStore.edit {
            it[TOKEN_KEY] = token
            it[DOMAIN_KEY]= domain
        }
    }

    suspend fun deleteToken() {
        context.dataStore.edit {
            it.clear()
        }
    }

    suspend fun getToken(): String? {
        val values = context.dataStore.data.first()
        val token = values[TOKEN_KEY]
        return token
    }

    suspend fun getDomain(): String? {
        val values = context.dataStore.data.first()
        val domain = values[DOMAIN_KEY]
        return domain
    }
}