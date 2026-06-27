package com.example.moodmate.domain.mood

import com.example.moodmate.domain.model.Mood
import com.example.moodmate.domain.model.FaceDetectionResult

class MoodEstimator {

    fun estimate(
        result: FaceDetectionResult
    ): Mood {

        if (result.faceCount == 0)
            return Mood.UNKNOWN

        val smile =
            result.smileProbability ?: 0f

        val leftEye =
            result.leftEyeOpenProbability ?: 0f

        val rightEye =
            result.rightEyeOpenProbability ?: 0f

        return when {

            smile > 0.75f ->
                Mood.HAPPY

            leftEye < 0.35f &&
                    rightEye < 0.35f ->
                Mood.TIRED

            else ->
                Mood.NEUTRAL
        }
    }
}