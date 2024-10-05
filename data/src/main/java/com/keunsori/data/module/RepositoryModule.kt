package com.keunsori.data.module

import com.keunsori.data.api.MainApiService
import com.keunsori.data.datasource.MainRemoteDataSource
import com.keunsori.data.repository.InGameRepositoryImpl
import com.keunsori.domain.repository.InGameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun provideMainRemoteDataSource(mainApiService: MainApiService) : MainRemoteDataSource {
        return MainRemoteDataSource(mainApiService)
    }

    @Provides
    fun provideInGameRepository(mainRemoteDataSource: MainRemoteDataSource): InGameRepository {
        return InGameRepositoryImpl(mainRemoteDataSource)
    }
}
