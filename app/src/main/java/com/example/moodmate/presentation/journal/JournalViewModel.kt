package com.example.moodmate.presentation.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodmate.data.local.MoodEntity
import com.example.moodmate.domain.model.Mood
import com.example.moodmate.domain.usecase.SaveMoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val saveMoodUseCase: SaveMoodUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(JournalState())
    val state = _state.asStateFlow()

    fun selectMood(mood: Mood) {
        _state.value = _state.value.copy(selectedMood = mood)
    }

    fun updateNote(note: String) {
        _state.value = _state.value.copy(note = note)
    }

    fun saveJournal() {
        val currentState = _state.value
        val mood = currentState.selectedMood ?: return

        viewModelScope.launch {
            saveMoodUseCase(
                MoodEntity(
                    mood = mood,
                    smileProbability = 0f,
                    createdAt = System.currentTimeMillis(),
                    note = currentState.note
                )
            )

            // Reset state
            _state.value = JournalState()
        }
    }
}

data class JournalState(
    val selectedMood: Mood? = null,
    val note: String = ""
)