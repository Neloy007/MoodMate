package com.example.moodmate.presentation.camera.analyzer

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class FaceAnalyzer : ImageAnalysis.Analyzer {
    override fun analyze(image: ImageProxy) {

        Log.d("MoodMate", "Frame received")

        image.close()
    }
}