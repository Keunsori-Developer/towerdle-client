package com.keunsori.data.datasource

import com.keunsori.data.api.ApiService
import com.keunsori.data.service.GoogleLoginService
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton

@ActivityScoped
class UserRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val googleLoginService: GoogleLoginService
) {
    fun example(): String{
        return "user"
    }

    suspend fun tryLogin(clientId: String): String? {
        return googleLoginService.googleLogin(clientId)
    }
}
