package com.example.moodmate.presentation.camera

import android.Manifest
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen() {

    val permissionState =
        rememberPermissionState(
            Manifest.permission.CAMERA
        )

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    if (permissionState.status.isGranted) {

        CameraPreview()

    } else {

        Text("Please allow camera permission")

    }
}