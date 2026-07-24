package com.example.moodmate.domain.repository

import com.example.moodmate.data.local.MoodEntity
import kotlinx.coroutines.flow.Flow

interface MoodRepository {

    suspend fun saveMood(
        mood: MoodEntity
    )

    suspend fun deleteMood(
        mood: MoodEntity
    )

    fun getMoodHistory(): Flow<List<MoodEntity>>

    suspend fun clearHistory()
}