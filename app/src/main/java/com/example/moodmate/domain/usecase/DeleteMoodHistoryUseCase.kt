package com.example.moodmate.domain.usecase

import com.example.moodmate.data.local.MoodEntity
import com.example.moodmate.domain.repository.MoodRepository
import javax.inject.Inject

class DeleteMoodUseCase @Inject constructor(
    private val repository: MoodRepository
) {

    suspend operator fun invoke(
        mood: MoodEntity
    ) {
        repository.deleteMood(mood)
    }
}