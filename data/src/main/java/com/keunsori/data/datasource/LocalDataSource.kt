package com.keunsori.data.datasource

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val REFRESH_TOKEN_KEY = stringPreferencesKey("REFRESH_TOKEN_KEY")
        val IS_GOOGLE_LOGGED_IN_KEY = booleanPreferencesKey("IS_GOOGLE_LOGGED_IN")
        val GOOGLE_ID_TOKEN = stringPreferencesKey("GOOGLE_ID_TOKEN")
    }

    private var _refreshToken: String = ""
    val refreshToken: String
        get() = _refreshToken


    private var _accessToken: String = ""
    val accessToken: String
        get() = _accessToken


    val isGoogleLoggedIn: Flow<Boolean?> = dataStore.data.map {
        it[IS_GOOGLE_LOGGED_IN_KEY] ?: false
    }

    val googleIdToken: Flow<String?> = dataStore.data.map {
        it[GOOGLE_ID_TOKEN] ?: ""
    }

    suspend fun init() {
        _refreshToken = dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {
            it[REFRESH_TOKEN_KEY] ?: ""
        }.first()
    }

    suspend fun setRefreshToken(
        refreshToken: String,
    ) {
        try {
            _refreshToken = refreshToken
            dataStore.edit {
                it[REFRESH_TOKEN_KEY] = refreshToken
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    suspend fun googleLogin(idToken: String) {
        try {
            dataStore.edit {
                it[IS_GOOGLE_LOGGED_IN_KEY] = true
                it[GOOGLE_ID_TOKEN] = idToken
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun setAccessToken(accessToken: String) {
        _accessToken = accessToken
    }

    suspend fun deleteToken() {
        setRefreshToken("")
        setAccessToken("")
    }
}