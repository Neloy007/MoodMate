package com.example.moodmate.presentation.camera

import androidx.lifecycle.ViewModel
import com.example.moodmate.domain.model.Mood
import com.example.moodmate.domain.mood.MoodEstimator
import com.example.moodmate.domain.model.FaceDetectionResult
import com.example.moodmate.domain.mood.FaceQuality
import com.example.moodmate.domain.mood.FaceQualityChecker
import com.example.moodmate.domain.mood.MoodStabilizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraViewModel : ViewModel() {

    private val moodEstimator = MoodEstimator()
    private val moodStabilizer = MoodStabilizer()
    private val qualityChecker = FaceQualityChecker()


    private val _faceResult = MutableStateFlow(FaceDetectionResult())
    val faceResult = _faceResult.asStateFlow()

    private val _mood = MutableStateFlow(Mood.UNKNOWN)
    val mood = _mood.asStateFlow()

    private val _faceQuality =
        MutableStateFlow<FaceQuality>(FaceQuality.NoFace)

    val faceQuality =
        _faceQuality.asStateFlow()

    fun updateFaceResult(result: FaceDetectionResult) {

        _faceResult.value = result

        val estimatedMood = moodEstimator.estimate(result)

        val stableMood = moodStabilizer.addMood(estimatedMood)

        _mood.value = stableMood

        _faceQuality.value =
            qualityChecker.check(result)
    }
}