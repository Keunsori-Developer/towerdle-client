package com.keunsori.towerdle.module

import com.keunsori.data.repository.UserRepositoryImpl
import com.keunsori.domain.repository.InGameRepository
import com.keunsori.domain.repository.UserRepository
import com.keunsori.domain.usecase.CheckAnswerUseCase
import com.keunsori.domain.usecase.GetQuizInfoUseCase
import com.keunsori.domain.usecase.GetUserInfoUseCase
import com.keunsori.domain.usecase.IsExistWordUseCase
import com.keunsori.domain.usecase.SendQuizResultUseCase
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
    fun provideUserUseCase(userRepository: UserRepository): UserUseCase {
        return UserUseCase(userRepository)
    }

    @Provides
    fun provideGetUserInfoUseCase(userRepository: UserRepository): GetUserInfoUseCase {
        return GetUserInfoUseCase(userRepository)
    }

    @Provides
    fun provideCheckAnswerUseCase(inGameRepository: InGameRepository): CheckAnswerUseCase {
        return CheckAnswerUseCase(inGameRepository)
    }

    @Provides
    fun provideGetQuizWordUseCase(inGameRepository: InGameRepository): GetQuizInfoUseCase {
        return GetQuizInfoUseCase(inGameRepository)
    }

    @Provides
    fun provideSendQuizResultUseCase(inGameRepository: InGameRepository): SendQuizResultUseCase {
        return SendQuizResultUseCase(inGameRepository)
    }

    @Provides
    fun provideIsExistWordUseCase(inGameRepository: InGameRepository): IsExistWordUseCase {
        return IsExistWordUseCase(inGameRepository)
    }
}
