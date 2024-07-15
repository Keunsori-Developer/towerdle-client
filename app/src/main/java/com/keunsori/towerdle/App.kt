package com.keunsori.towerdle

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){
    companion object {
        val Context.dataStore by preferencesDataStore(name = "data")
    }
}