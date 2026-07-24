package com.example.moodmate.domain.usecase

import com.example.moodmate.domain.repository.MoodRepository
import javax.inject.Inject

class GetMoodHistoryUseCase @Inject constructor(
    private val repository: MoodRepository
) {

    operator fun invoke() =
        repository.getMoodHistory()
}