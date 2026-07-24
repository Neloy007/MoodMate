package com.example.moodmate.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodmate.data.local.MoodEntity
import com.example.moodmate.domain.usecase.DeleteMoodUseCase
import com.example.moodmate.domain.usecase.GetMoodHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getMoodHistoryUseCase: GetMoodHistoryUseCase,
    private val deleteMoodUseCase: DeleteMoodUseCase
) : ViewModel() {

    val moods = getMoodHistoryUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun deleteMood(
        mood: MoodEntity
    ) {
        viewModelScope.launch {
            deleteMoodUseCase(mood)
        }
    }
}