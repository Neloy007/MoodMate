package com.example.moodmate.presentation.result

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
class ResultViewModel @Inject constructor(
    private val saveMoodUseCase: SaveMoodUseCase
) : ViewModel() {

    private val _isSaved = MutableStateFlow(false)
    val isSaved = _isSaved.asStateFlow()

    fun saveMoodWithNote(
        mood: Mood,
        smileProbability: Float,
        note: String
    ) {
        viewModelScope.launch {
            saveMoodUseCase(
                MoodEntity(
                    mood = mood,
                    smileProbability = smileProbability,
                    createdAt = System.currentTimeMillis(),
                    note = if (note.isNotEmpty()) note else null
                )
            )
            _isSaved.value = true
        }
    }

    fun resetSavedState() {
        _isSaved.value = false
    }
}