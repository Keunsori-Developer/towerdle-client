package com.keunsori.presentation.module

import android.content.Context
import com.keunsori.presentation.utils.GifLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GifLoaderModule {
    @Provides
    fun provideGifLoader(@ApplicationContext context: Context): GifLoader {
        return GifLoader(context)
    }
}
