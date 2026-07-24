package com.example.moodmate.di

import com.example.moodmate.data.repository.MoodRepositoryImpl
import com.example.moodmate.domain.repository.MoodRepository
import com.example.moodmate.data.local.MoodDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMoodRepository(
        dao: MoodDao
    ): MoodRepository {

        return MoodRepositoryImpl(dao)
    }
}