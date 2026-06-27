package com.example.moodmate.presentation.camera

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraViewModel : ViewModel() {

    private val _faceCount = MutableStateFlow(0)
    val faceCount = _faceCount.asStateFlow()

    fun updateFaceCount(count: Int) {
        _faceCount.value = count
    }
}