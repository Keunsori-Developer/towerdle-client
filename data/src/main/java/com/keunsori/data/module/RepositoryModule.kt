package com.keunsori.data.module

import com.keunsori.data.repository.InGameRepositoryImpl
import com.keunsori.domain.repository.InGameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideInGameRepository(): InGameRepository {
        return InGameRepositoryImpl()
    }
}