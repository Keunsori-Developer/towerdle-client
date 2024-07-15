package com.keunsori.towerdle.data.repository

import com.keunsori.towerdle.data.datasource.LocalDataSource
import com.keunsori.towerdle.data.datasource.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val localDataSource: LocalDataSource
) {
    fun getTestDataSource(): Flow<String?> {
        return localDataSource.getTest()
    }

    suspend fun setTestDataSource(test: String){
        localDataSource.setTest(test)
    }
}