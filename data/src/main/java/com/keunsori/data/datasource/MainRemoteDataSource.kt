package com.keunsori.data.datasource


import com.keunsori.data.api.ApiService
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    fun example(): String{
        return "main"
    }
}

