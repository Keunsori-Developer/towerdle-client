package com.keunsori.data.datasource

import com.keunsori.data.api.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) {
    fun example(): String{
        return "user"
    }
}
