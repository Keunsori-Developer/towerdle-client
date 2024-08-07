package com.keunsori.data.repository

import com.keunsori.data.datasource.LocalDataSource
import com.keunsori.data.datasource.MainRemoteDataSource
import com.keunsori.domain.repository.MainRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val mainRemoteDataSource: MainRemoteDataSource,
    private val localDataSource: LocalDataSource
) : MainRepository {

    override suspend fun test(test: String): String {
        return mainRemoteDataSource.example()
    }
}