package com.keunsori.data.module

import com.keunsori.data.api.MainApiService
import com.keunsori.data.datasource.LocalDataSource
import com.keunsori.data.datasource.MainRemoteDataSource
import com.keunsori.data.datasource.UserRemoteDataSource
import com.keunsori.data.repository.InGameRepositoryImpl
import com.keunsori.data.repository.UserRepositoryImpl
import com.keunsori.domain.repository.InGameRepository
import com.keunsori.domain.repository.UserRepository
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

    @Provides
    fun provideUserRepository(userRemoteDataSource: UserRemoteDataSource, localDataSource: LocalDataSource): UserRepository {
        return UserRepositoryImpl(userRemoteDataSource, localDataSource)
    }
}
