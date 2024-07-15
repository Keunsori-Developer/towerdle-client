package com.keunsori.towerdle.data.repository

import com.keunsori.towerdle.data.datasource.LocalDataSource
import com.keunsori.towerdle.data.datasource.MainRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val mainRemoteDataSource: MainRemoteDataSource,
    private val localDataSource: LocalDataSource
) {
    fun example(){
        //mainRemoteDataSource.example()
    }
}