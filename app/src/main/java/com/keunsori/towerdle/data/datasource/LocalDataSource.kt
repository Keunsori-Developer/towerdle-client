package com.keunsori.towerdle.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val TEST_KEY = stringPreferencesKey("TEST_KEY")
    }

    private val testFlow: Flow<String?> = dataStore.data.map {
        it[TEST_KEY]
    }

    suspend fun setTest(
        test: String,
    ) {
        dataStore.edit {
            it[TEST_KEY] = test
        }
    }

    fun getTest(): Flow<String?> {
        return testFlow
    }
}