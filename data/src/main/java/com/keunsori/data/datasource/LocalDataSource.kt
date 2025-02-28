package com.keunsori.data.datasource

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
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
        val GUEST_ID_TOKEN = stringPreferencesKey("GUEST_ID_TOKEN")
        val CHALLENGE_MODE_DATA = stringSetPreferencesKey("CHALLENGE_MODE_DATA")
        val CHALLENGE_MODE_SAVED_DATE = stringPreferencesKey("CHALLENGE_MODE_SAVED_DATE")
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

    val guestIdToken: Flow<String?> = dataStore.data.map {
        it[GUEST_ID_TOKEN] ?: ""
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

    suspend fun googleLogin() {
        try {
            dataStore.edit {
                it[IS_GOOGLE_LOGGED_IN_KEY] = true
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    suspend fun guestLogin(guestIdToken: String) {
        try {
            dataStore.edit {
                it[GUEST_ID_TOKEN] = guestIdToken
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun setAccessToken(accessToken: String) {
        _accessToken = accessToken
    }

    suspend fun deleteToken() {
        dataStore.edit {
            it[IS_GOOGLE_LOGGED_IN_KEY] = false
        }
        setRefreshToken("")
        setAccessToken("")
    }

    suspend fun updateChallengeModeData(jsonString: String?, date: String) {
        try {
            dataStore.edit { preferences ->
                if (jsonString == null) {
                    preferences.remove(CHALLENGE_MODE_DATA)
                    preferences.remove(CHALLENGE_MODE_SAVED_DATE)
                } else {
                    val savedDate = preferences[CHALLENGE_MODE_SAVED_DATE]
                    if (savedDate != date) {
                        preferences[CHALLENGE_MODE_SAVED_DATE] = date
                        preferences.remove(CHALLENGE_MODE_DATA)
                    }
                    // 기존에 저장된 Set<String> 가져오기 (없으면 빈 Set 반환)
                    val currentSet = preferences[CHALLENGE_MODE_DATA] ?: emptySet()

                    // 새로운 String을 Set에 추가
                    val updatedSet = currentSet + jsonString

                    // 업데이트된 Set 저장
                    preferences[CHALLENGE_MODE_DATA] = updatedSet
                    preferences[CHALLENGE_MODE_SAVED_DATE] = date
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getChallengeModeData(date: String): Flow<Set<String>?> {
        return dataStore.data.map { preferences ->
            val savedDate = preferences[CHALLENGE_MODE_SAVED_DATE]
            if (savedDate == date) preferences[CHALLENGE_MODE_DATA] else null
        }
    }
}
