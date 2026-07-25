package com.example.moodmate.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodmate.domain.repository.MoodRepository
import com.example.moodmate.domain.usecase.GetMoodHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getMoodHistoryUseCase: GetMoodHistoryUseCase,
    private val moodRepository: MoodRepository
) : ViewModel() {

    // All moods from database
    val moods = getMoodHistoryUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    // Clear all data
    suspend fun clearAllData() {
        moodRepository.clearHistory()
    }
}