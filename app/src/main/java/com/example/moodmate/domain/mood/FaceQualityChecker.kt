package com.example.moodmate.domain.mood

import com.example.moodmate.domain.model.FaceDetectionResult

class FaceQualityChecker {

    companion object {
        private const val EYE_OPEN_THRESHOLD = 0.35f
    }

    fun check(
        result: FaceDetectionResult
    ): FaceQuality {

        if (result.faceCount == 0) {
            return FaceQuality.NoFace
        }

        if (result.faceCount > 1) {
            return FaceQuality.MultipleFaces
        }

        val leftEye = result.leftEyeOpenProbability ?: 0f
        val rightEye = result.rightEyeOpenProbability ?: 0f

        if (
            leftEye < EYE_OPEN_THRESHOLD &&
            rightEye < EYE_OPEN_THRESHOLD
        ) {
            return FaceQuality.EyesClosed
        }

        return FaceQuality.Good
    }
}