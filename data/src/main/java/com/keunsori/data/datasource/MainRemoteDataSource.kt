package com.keunsori.data.datasource


import com.keunsori.data.api.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    fun example(): String{
        val res = apiService.getQuizWord().execute()
        return res.body()?.value ?: "분홍"
    }
}

