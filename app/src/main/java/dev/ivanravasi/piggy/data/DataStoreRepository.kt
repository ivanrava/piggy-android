package dev.ivanravasi.piggy.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


class DataStoreRepository(private val context: Context) {
    private companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        val TOKEN_KEY = stringPreferencesKey("token")
        val DOMAIN_KEY = stringPreferencesKey("domain")
        val MATERIAL_YOU_KEY = booleanPreferencesKey("materialyou")
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

    suspend fun toggleMaterialYou() {
        context.dataStore.edit {
            it[MATERIAL_YOU_KEY] = ! (it[MATERIAL_YOU_KEY] ?: false)
        }
    }

    suspend fun isMaterialYouEnabled(): Boolean? {
        val values = context.dataStore.data.first()
        return values[MATERIAL_YOU_KEY]
    }
}