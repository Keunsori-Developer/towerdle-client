package com.keunsori.data

import com.keunsori.data.datasource.LocalDataSource
import com.keunsori.data.datasource.MainRemoteDataSource
import com.keunsori.domain.repository.MainRepository
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton

@ActivityScoped
class MainRepositoryImpl @Inject constructor(
    private val mainRemoteDataSource: MainRemoteDataSource,
    private val localDataSource: LocalDataSource
) : MainRepository {

    override suspend fun test(test: String): String {
        return mainRemoteDataSource.example()
    }
}