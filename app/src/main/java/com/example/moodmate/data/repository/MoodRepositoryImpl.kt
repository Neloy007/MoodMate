package com.example.moodmate.data.repository

import com.example.moodmate.data.local.MoodDao
import com.example.moodmate.data.local.MoodEntity
import com.example.moodmate.domain.repository.MoodRepository
import kotlinx.coroutines.flow.Flow

class MoodRepositoryImpl(
    private val dao: MoodDao
) : MoodRepository {

    override suspend fun saveMood(
        mood: MoodEntity
    ) {
        dao.insertMood(mood)
    }

    override suspend fun deleteMood(
        mood: MoodEntity
    ) {
        dao.deleteMood(mood)
    }

    override fun getMoodHistory(): Flow<List<MoodEntity>> {
        return dao.getAllMoods()
    }

    override suspend fun clearHistory() {
        dao.deleteAll()
    }
}