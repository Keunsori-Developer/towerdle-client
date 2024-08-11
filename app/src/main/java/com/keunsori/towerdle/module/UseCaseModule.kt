package com.keunsori.towerdle.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.keunsori.data.repository.MainRepositoryImpl
import com.keunsori.data.repository.UserRepositoryImpl
import com.keunsori.domain.repository.MainRepository
import com.keunsori.domain.repository.UserRepository
import com.keunsori.domain.usecase.MainUseCase
import com.keunsori.domain.usecase.UserUseCase
import com.keunsori.towerdle.App.Companion.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    // domain module은 hilt를 사용하지 않으므로 정의 필요
    @Provides
    fun provideMainUseCase(mainRepository: MainRepositoryImpl): MainUseCase {
        return MainUseCase(mainRepository)
    }

    @Provides
    fun provideUserUseCase(userRepository: UserRepositoryImpl): UserUseCase {
        return UserUseCase(userRepository)
    }
}
