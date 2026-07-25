package com.example.moodmate.presentation.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodmate.data.local.MoodEntity
import com.example.moodmate.domain.model.FaceDetectionResult
import com.example.moodmate.domain.model.Mood
import com.example.moodmate.domain.mood.FaceQuality
import com.example.moodmate.domain.mood.FaceQualityChecker
import com.example.moodmate.domain.mood.MoodEstimator
import com.example.moodmate.domain.mood.MoodStabilizer
import com.example.moodmate.domain.usecase.SaveMoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val moodEstimator: MoodEstimator,
    private val moodStabilizer: MoodStabilizer,
    private val qualityChecker: FaceQualityChecker,
    private val saveMoodUseCase: SaveMoodUseCase
) : ViewModel() {

    private val _faceResult = MutableStateFlow(FaceDetectionResult())
    val faceResult = _faceResult.asStateFlow()

    private val _mood = MutableStateFlow(Mood.UNKNOWN)
    val mood = _mood.asStateFlow()

    private val _faceQuality = MutableStateFlow<FaceQuality>(FaceQuality.NoFace)
    val faceQuality = _faceQuality.asStateFlow()

    fun updateFaceResult(result: FaceDetectionResult) {
        _faceResult.value = result
        val estimatedMood = moodEstimator.estimate(result)
        val stableMood = moodStabilizer.addMood(estimatedMood)
        _mood.value = stableMood
        _faceQuality.value = qualityChecker.check(result)
    }

    fun saveMood() {
        viewModelScope.launch {
            saveMoodUseCase(
                MoodEntity(
                    mood = mood.value,
                    smileProbability = faceResult.value.smileProbability ?: 0f,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }

    // Helper function to get current mood and smile probability
    fun getCurrentMoodData(): Pair<Mood, Float> {
        return Pair(
            mood.value,
            faceResult.value.smileProbability ?: 0f
        )
    }
}