package com.example.moodmate.presentation.camera

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moodmate.presentation.camera.components.MoodInfoCard
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    onSaved: () -> Unit
) {

    val permissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )

    val viewModel: CameraViewModel = hiltViewModel()

    val faceResult by viewModel.faceResult.collectAsStateWithLifecycle()
    val mood by viewModel.mood.collectAsStateWithLifecycle()
    val faceQuality by viewModel.faceQuality.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    if (permissionState.status.isGranted) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                onFacesDetected = viewModel::updateFaceResult
            )

            MoodInfoCard(
                mood = mood,
                faceResult = faceResult,
                faceQuality = faceQuality,
                onSaveClick = {

                    viewModel.saveMood()

                    onSaved()

                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }

    } else {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Please allow camera permission."
            )

        }

    }
}