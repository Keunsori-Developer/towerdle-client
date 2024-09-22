package com.keunsori.data.datasource

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.scopes.ActivityScoped
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
    }

    private var _refreshToken: String = ""
    val refreshToken: String
        get() = _refreshToken


    private var _accessToken: String = ""
    val accessToken: String
        get() = _accessToken

    private var _isGuest: Boolean = true
    val isGuest: Boolean
        get() = _isGuest

    suspend fun initRefreshToken() {
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

    fun setAccessToken(accessToken: String) {
        _accessToken = accessToken
    }

    fun setLoginType(isGuest: Boolean){
        _isGuest = isGuest
    }

    suspend fun deleteToken() {
        setRefreshToken("")
        setAccessToken("")
    }
}