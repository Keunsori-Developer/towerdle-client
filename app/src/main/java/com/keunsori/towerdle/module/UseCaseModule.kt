package com.keunsori.towerdle.module

import com.keunsori.data.repository.UserRepositoryImpl
import com.keunsori.domain.repository.InGameRepository
import com.keunsori.domain.usecase.CheckAnswerUseCase
import com.keunsori.domain.usecase.GetQuizInfoUseCase
import com.keunsori.domain.usecase.UserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    // domain module은 hilt를 사용하지 않으므로 정의 필요

    @Provides
    fun provideUserUseCase(userRepository: UserRepositoryImpl): UserUseCase {
        return UserUseCase(userRepository)
    }

    @Provides
    fun provideCheckAnswerUseCase(inGameRepository: InGameRepository): CheckAnswerUseCase {
        return CheckAnswerUseCase(inGameRepository)
    }

    @Provides
    fun provideGetQuizWordUseCase(inGameRepository: InGameRepository): GetQuizInfoUseCase {
        return GetQuizInfoUseCase(inGameRepository)
    }
}
