package com.example.moodmate.presentation.camera

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen() {

    val permissionState =
        rememberPermissionState(
            Manifest.permission.CAMERA
        )

    val viewModel: CameraViewModel = viewModel()

    val faceCount by viewModel.faceCount.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    if (permissionState.status.isGranted) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                onFacesDetected = viewModel::updateFaceCount
            )

            Text(
                text = "Faces: $faceCount",
                modifier = Modifier.align(Alignment.TopCenter),
                style = MaterialTheme.typography.headlineSmall
            )
        }

    } else {

        Text("Please allow camera permission")

    }
}