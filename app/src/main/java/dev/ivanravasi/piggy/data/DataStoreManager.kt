package dev.ivanravasi.piggy.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManager(val context: Context) {
    companion object {
        val TOKEN_KEY = stringPreferencesKey("token")
        val DOMAIN_KEY = stringPreferencesKey("domain")
    }

    public suspend fun saveTokenAndDomain(token: String, domain: String) {
        context.dataStore.edit {
            it[TOKEN_KEY] = token
            it[DOMAIN_KEY]= domain
        }
    }

    public suspend fun deleteToken() {
        context.dataStore.edit {
            it.clear()
        }
    }

    public suspend fun getToken(): String? {
        val values = context.dataStore.data.first()
        val token = values[TOKEN_KEY]
        return token
    }

    public suspend fun getDomain(): String? {
        val values = context.dataStore.data.first()
        val domain = values[DOMAIN_KEY]
        return domain
    }
}