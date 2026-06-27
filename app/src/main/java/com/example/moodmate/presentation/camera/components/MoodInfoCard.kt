package com.example.moodmate.presentation.camera.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = when (mood) {
                    Mood.HAPPY -> "😊 Happy"
                    Mood.NEUTRAL -> "😐 Neutral"
                    Mood.TIRED -> "😴 Tired"
                    Mood.UNKNOWN -> "❓ No Face"
                },
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Smile: ${
                    ((faceResult.smileProbability ?: 0f) * 100).toInt()
                }%"
            )

//            Text(
//                text = if (faceResult.faceCount > 0)
//                    "👤 Face Detected"
//                else
//                    "🚫 No Face Detected"
//            )
            Text(
                text = when(faceQuality) {

                    FaceQuality.Good ->
                        "✅ Ready"

                    FaceQuality.NoFace ->
                        "📷 No face detected"

                    FaceQuality.MultipleFaces ->
                        "👥 Only one face allowed"

                    FaceQuality.EyesClosed ->
                        "👀 Open your eyes"
                }
            )

            Button(
                onClick = onSaveClick,
//                enabled = faceResult.faceCount > 0
                enabled = faceQuality == FaceQuality.Good

            ) {
                Text("Save Mood")
            }
        }
    }
}