package com.example.moodmate.domain.mood

sealed interface FaceQuality {

    data object Good : FaceQuality

    data object NoFace : FaceQuality

    data object MultipleFaces : FaceQuality

    data object EyesClosed : FaceQuality
}