package com.example.moodmate.presentation.camera

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.moodmate.presentation.camera.analyzer.FaceAnalyzer

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Create PreviewView only once
    val previewView = remember {
        PreviewView(context)
    }

    DisposableEffect(Unit) {

        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(context)

        val executor = ContextCompat.getMainExecutor(context)

        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()

            preview.surfaceProvider = previewView.surfaceProvider

            val imageAnalysis = ImageAnalysis.Builder()
                .build()

            imageAnalysis.setAnalyzer(
                executor,
                FaceAnalyzer()
            )

            val cameraSelector =
                CameraSelector.DEFAULT_FRONT_CAMERA

            try {

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                            imageAnalysis

                )

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, executor)

        onDispose {
            cameraProviderFuture.get().unbindAll()
        }
    }

    AndroidView(
        factory = {
            previewView
        },
        modifier = modifier
    )
}