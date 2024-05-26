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
        val REMEMBER_KEY = booleanPreferencesKey("remember")
    }

    public suspend fun saveAuthData(token: String, domain: String, remember: Boolean) {
        context.dataStore.edit {
            it[TOKEN_KEY] = token
            it[DOMAIN_KEY]= domain
            it[REMEMBER_KEY] = remember
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

    public suspend fun shouldRemember(): Boolean {
        val values = context.dataStore.data.first()
        val remember = values[REMEMBER_KEY]
        return remember!!
    }
}