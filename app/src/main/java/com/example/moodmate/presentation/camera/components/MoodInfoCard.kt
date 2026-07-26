package com.example.moodmate.presentation.camera.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodmate.domain.model.Mood
import com.example.moodmate.domain.model.FaceDetectionResult
import com.example.moodmate.domain.mood.FaceQuality
import com.example.moodmate.presentation.analytics.getMoodColor
import com.example.moodmate.presentation.analytics.getMoodEmoji

private val PurplePrimary = Color(0xFF7C6FE0)
private val PurpleDark = Color(0xFF1A1A2E)
private val TextGray = Color(0xFF8A8A9A)
private val GreenAccent = Color(0xFF34B36B)
private val GreenBgLight = Color(0xFFE1F5E9)

/**
 * Bottom status sheet for the Check Mood screen.
 * The mood shown here is fully automatic — driven by live face
 * detection results — there is no manual mood selection.
 */
@Composable
fun MoodInfoCard(
    mood: Mood,
    faceResult: FaceDetectionResult,
    faceQuality: FaceQuality,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isReady = faceQuality == FaceQuality.Good
    val confidence = faceResult.smileProbability ?: 0f
    val confidencePct = (confidence * 100).toInt()

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        color = Color.White,
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            // Drag handle
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFE4E1F0))
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Mood header row — updates live from detection, no taps needed
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(getMoodColor(mood).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = getMoodEmoji(mood), fontSize = 32.sp)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = GreenBgLight
                        ) {
                            Text(
                                text = "Current Mood",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = GreenAccent,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = getMoodLabel(mood),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = PurpleDark
                        )
                    }
                }

                if (isReady) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = GreenBgLight
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$confidencePct%",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenAccent
                            )
                            Text(
                                text = "Confidence",
                                fontSize = 10.sp,
                                color = GreenAccent
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Status / feedback line — reacts automatically to detection state
            Text(
                text = when {
                    !isReady && faceQuality == FaceQuality.NoFace -> "Position your face in the frame."
                    !isReady && faceQuality == FaceQuality.MultipleFaces -> "Only one face allowed at a time."
                    !isReady && faceQuality == FaceQuality.EyesClosed -> "Please open your eyes."
                    else -> getMoodStatusLine(mood)
                },
                fontSize = 14.sp,
                color = TextGray
            )

            if (isReady) {
                Spacer(modifier = Modifier.height(14.dp))
                LinearProgressIndicator(
                    progress = confidence,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = GreenAccent,
                    trackColor = GreenBgLight
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Save button
            Button(
                onClick = onSaveClick,
                enabled = isReady,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurplePrimary,
                    disabledContainerColor = PurplePrimary.copy(alpha = 0.35f)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Save Mood",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

private fun getMoodLabel(mood: Mood): String = when (mood) {
    Mood.HAPPY -> "Happy"
    Mood.NEUTRAL -> "Neutral"
    Mood.TIRED -> "Tired"
    Mood.UNKNOWN -> "No Face"
}

private fun getMoodStatusLine(mood: Mood): String = when (mood) {
    Mood.HAPPY -> "You seem to be feeling good! 💚"
    Mood.NEUTRAL -> "You look calm and balanced."
    Mood.TIRED -> "You might need some rest."
    Mood.UNKNOWN -> "We couldn't read your expression."
}