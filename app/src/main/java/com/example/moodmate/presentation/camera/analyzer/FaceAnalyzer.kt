package com.example.moodmate.presentation.camera.analyzer

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.moodmate.domain.model.FaceDetectionResult
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceAnalyzer(
    private val onFacesDetected: (FaceDetectionResult) -> Unit
) : ImageAnalysis.Analyzer {

    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setClassificationMode(
            FaceDetectorOptions.CLASSIFICATION_MODE_ALL
        )
        .enableTracking()
        .build()

    private val detector = FaceDetection.getClient(options)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {

        val mediaImage = imageProxy.image

        if (mediaImage != null) {

            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            detector.process(image)
                .addOnSuccessListener { faces ->
                    val firstFace = faces.firstOrNull()

                    val result = FaceDetectionResult(
                        faceCount = faces.size,
                        smileProbability = firstFace?.smilingProbability,
                        leftEyeOpenProbability = firstFace?.leftEyeOpenProbability,
                        rightEyeOpenProbability = firstFace?.rightEyeOpenProbability,
                        trackingId = firstFace?.trackingId
                    )

                    onFacesDetected(result)
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }

        } else {
            imageProxy.close()
        }
    }
}