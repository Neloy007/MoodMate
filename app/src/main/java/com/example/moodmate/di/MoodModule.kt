package com.example.moodmate.di

import com.example.moodmate.domain.mood.FaceQualityChecker
import com.example.moodmate.domain.mood.MoodEstimator
import com.example.moodmate.domain.mood.MoodStabilizer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MoodModule {

    @Provides
    fun provideMoodEstimator() =
        MoodEstimator()

    @Provides
    fun provideMoodStabilizer() =
        MoodStabilizer()

    @Provides
    fun provideFaceQualityChecker() =
        FaceQualityChecker()
}