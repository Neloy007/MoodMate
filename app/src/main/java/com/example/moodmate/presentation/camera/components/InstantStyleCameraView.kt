package com.example.moodmate.presentation.camera.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodmate.domain.model.FaceDetectionResult
import com.example.moodmate.presentation.camera.CameraPreview

@Composable
fun InstantStyleCameraView(
    onFacesDetected: (FaceDetectionResult) -> Unit,
    modifier: Modifier = Modifier,
    boxSize: Int = 350,
    showHoldText: Boolean = true
) {
    val density = LocalDensity.current

    // Animated rotation for the gradient border
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Pulse animation
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Main box container
        Box(
            modifier = Modifier
                .size(boxSize.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(
                    width = 2.dp,
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Camera preview inside box
            CameraPreview(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                onFacesDetected = onFacesDetected,
                isCircular = false
            )

            // Animated gradient border overlay
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val borderWidth = with(density) { 3.dp.toPx() }
                val cornerRadius = with(density) { 20.dp.toPx() }

                rotate(rotation) {
                    drawRoundRect(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color(0xFF4A90D9).copy(alpha = 0.9f),
                                Color(0xFF6A5ACD).copy(alpha = 0.6f),
                                Color(0xFF4A90D9).copy(alpha = 0.9f)
                            )
                        ),
                        topLeft = Offset(borderWidth / 2, borderWidth / 2),
                        size = Size(
                            size.width - borderWidth,
                            size.height - borderWidth
                        ),
                        cornerRadius = CornerRadius(cornerRadius - borderWidth / 2),
                        style = Stroke(
                            width = borderWidth,
                            cap = StrokeCap.Round
                        )
                    )
                }
            }

            // Corner accents
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                val cornerSize = with(density) { 40.dp.toPx() }
                val strokeWidth = with(density) { 3.dp.toPx() }
                val padding = with(density) { 0.dp.toPx() }

                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Top-left corner
                    drawLine(
                        color = Color(0xFF4A90D9).copy(alpha = 0.8f),
                        start = Offset(padding, padding + cornerSize),
                        end = Offset(padding, padding),
                        strokeWidth = strokeWidth
                    )
                    drawLine(
                        color = Color(0xFF4A90D9).copy(alpha = 0.8f),
                        start = Offset(padding, padding),
                        end = Offset(padding + cornerSize, padding),
                        strokeWidth = strokeWidth
                    )

                    // Top-right corner
                    drawLine(
                        color = Color(0xFF4A90D9).copy(alpha = 0.8f),
                        start = Offset(size.width - padding, padding + cornerSize),
                        end = Offset(size.width - padding, padding),
                        strokeWidth = strokeWidth
                    )
                    drawLine(
                        color = Color(0xFF4A90D9).copy(alpha = 0.8f),
                        start = Offset(size.width - padding, padding),
                        end = Offset(size.width - padding - cornerSize, padding),
                        strokeWidth = strokeWidth
                    )

                    // Bottom-left corner
                    drawLine(
                        color = Color(0xFF4A90D9).copy(alpha = 0.8f),
                        start = Offset(padding, size.height - padding - cornerSize),
                        end = Offset(padding, size.height - padding),
                        strokeWidth = strokeWidth
                    )
                    drawLine(
                        color = Color(0xFF4A90D9).copy(alpha = 0.8f),
                        start = Offset(padding, size.height - padding),
                        end = Offset(padding + cornerSize, size.height - padding),
                        strokeWidth = strokeWidth
                    )

                    // Bottom-right corner
                    drawLine(
                        color = Color(0xFF4A90D9).copy(alpha = 0.8f),
                        start = Offset(size.width - padding, size.height - padding - cornerSize),
                        end = Offset(size.width - padding, size.height - padding),
                        strokeWidth = strokeWidth
                    )
                    drawLine(
                        color = Color(0xFF4A90D9).copy(alpha = 0.8f),
                        start = Offset(size.width - padding, size.height - padding),
                        end = Offset(size.width - padding - cornerSize, size.height - padding),
                        strokeWidth = strokeWidth
                    )
                }
            }

            // Subtle glow overlay
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawRoundRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.03f),
                            Color.Transparent
                        ),
                        radius = size.minDimension / 2
                    ),
                    cornerRadius = CornerRadius(with(density) { 20.dp.toPx() })
                )
            }

            // Face detection status indicator
            if (showHoldText) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Canvas(modifier = Modifier.size(8.dp)) {
                        drawCircle(
                            color = Color(0xFF4CAF50),
                            radius = with(density) { 4.dp.toPx() }
                        )
                    }
                }
            }
        }

        // "HOLD TO INSTANT" label
        if (showHoldText) {
            Text(
                text = "HOLD TO INSTANT",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 3.sp,
                    fontSize = 12.sp
                ),
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            )
        }
    }
}