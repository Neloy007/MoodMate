package com.example.moodmate.presentation.camera.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.moodmate.domain.model.Mood
import com.example.moodmate.domain.model.FaceDetectionResult
import com.example.moodmate.domain.mood.FaceQuality

@Composable
fun MoodInfoCard(
    mood: Mood,
    faceResult: FaceDetectionResult,
    faceQuality: FaceQuality,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isReady = faceQuality == FaceQuality.Good

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A2E).copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Mood emoji and name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = when (mood) {
                        Mood.HAPPY -> "😊"
                        Mood.NEUTRAL -> "😐"
                        Mood.TIRED -> "😴"
                        Mood.UNKNOWN -> "❓"
                    },
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = when (mood) {
                        Mood.HAPPY -> "Happy"
                        Mood.NEUTRAL -> "Neutral"
                        Mood.TIRED -> "Tired"
                        Mood.UNKNOWN -> "No Face"
                    },
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

            // Smile percentage
            if (faceResult.faceCount > 0) {
                Text(
                    text = "Smile: ${((faceResult.smileProbability ?: 0f) * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            // Status text
            val statusText = when (faceQuality) {
                FaceQuality.Good -> "✅ Ready to save"
                FaceQuality.NoFace -> "📷 No face detected"
                FaceQuality.MultipleFaces -> "👥 Only one face allowed"
                FaceQuality.EyesClosed -> "👀 Open your eyes"
            }

            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isReady) Color(0xFF4CAF50) else Color.White.copy(alpha = 0.7f)
            )

            // Save button
            Button(
                onClick = onSaveClick,
                enabled = isReady,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isReady)
                        Color(0xFF4A90D9)
                    else
                        Color(0xFF4A90D9).copy(alpha = 0.3f)
                )
            ) {
                Text(
                    text = "Save Mood",
                    color = if (isReady) Color.White else Color.White.copy(alpha = 0.5f),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}