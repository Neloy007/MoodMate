package com.example.moodmate.presentation.camera.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.moodmate.domain.model.FaceDetectionResult
import com.example.moodmate.presentation.camera.CameraPreview

@Composable
fun InstantStyleCameraView(
    onFacesDetected: (FaceDetectionResult) -> Unit,
    modifier: Modifier = Modifier,
    onCapture: () -> Unit = {},
    onFlipCamera: () -> Unit = {},
    cornerRadius: Int = 64
) {
    var isFlashOn by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(cornerRadius.dp)

    Box(
        modifier = modifier
            .shadow(
                elevation = 24.dp,
                shape = shape,
                ambientColor = Color(0xFF7C6FE0).copy(alpha = 0.35f),
                spotColor = Color(0xFF7C6FE0).copy(alpha = 0.35f)
            )
            .clip(shape),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Live camera feed
        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            onFacesDetected = onFacesDetected,
            isCircular = false
        )

        // Soft gradient at the bottom so the control icons stay legible
        // over whatever the camera is showing
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.4f)
                        )
                    )
                )
        )

        // Bottom control row: flash / shutter / flip
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 36.dp, vertical = 22.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            CameraCircleButton(
//                icon = if (isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
//                size = 48.dp,
//                onClick = { isFlashOn = !isFlashOn }
//            )
//
//            ShutterButton(onClick = onCapture)
//
//            CameraCircleButton(
//                icon = Icons.Default.Autorenew,
//                size = 48.dp,
//                onClick = onFlipCamera
//            )
//        }
    }
}

@Composable
private fun CameraCircleButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    size: Dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.35f)),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun ShutterButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(76.dp)
            .clip(CircleShape)
            .border(width = 4.dp, color = Color.White.copy(alpha = 0.65f), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(76.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}