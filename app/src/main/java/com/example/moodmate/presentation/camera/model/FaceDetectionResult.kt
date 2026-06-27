package com.example.moodmate.presentation.camera.model

data class FaceDetectionResult(
    val faceCount: Int = 0,
    val smileProbability: Float? = null,
    val leftEyeOpenProbability: Float? = null,
    val rightEyeOpenProbability: Float? = null,
    val trackingId: Int? = null
)