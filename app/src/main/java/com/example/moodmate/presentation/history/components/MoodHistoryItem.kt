package com.example.moodmate.presentation.history.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodmate.data.local.MoodEntity
import com.example.moodmate.domain.model.Mood
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.platform.LocalLocale

@Composable
fun MoodHistoryItem(
    mood: MoodEntity,
    onDelete: () -> Unit
) {
    val date = SimpleDateFormat(
        "dd MMM yyyy • hh:mm a",
        LocalLocale.current.platformLocale
    ).format(Date(mood.createdAt))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Mood Emoji with background
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = getMoodColor(mood.mood).copy(alpha = 0.15f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = getMoodEmoji(mood.mood),
                            fontSize = 24.sp
                        )
                    }
                }

                // Mood details
                Column {
                    Text(
                        text = mood.mood.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A2E)
                        )
                    )

                    Text(
                        text = "Smile: ${(mood.smileProbability * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF666666)
                        )
                    )

                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF888888)
                        )
                    )

                    // Show note preview if exists
                    if (!mood.note.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF5F8FF)
                        ) {
                            Text(
                                text = "📝 ${mood.note.take(50)}${if (mood.note.length > 50) "..." else ""}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF666666)
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }

            // Delete button
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFFF5252).copy(alpha = 0.7f)
                )
            }
        }
    }
}

fun getMoodEmoji(mood: Mood): String {
    return when (mood) {
        Mood.HAPPY -> "😊"
        Mood.NEUTRAL -> "😐"
        Mood.TIRED -> "😴"
        Mood.UNKNOWN -> "❓"
    }
}

fun getMoodColor(mood: Mood): Color {
    return when (mood) {
        Mood.HAPPY -> Color(0xFF4CAF50)  // Green
        Mood.NEUTRAL -> Color(0xFFFF9800) // Orange
        Mood.TIRED -> Color(0xFF9C27B0)   // Purple
        Mood.UNKNOWN -> Color(0xFF9E9E9E) // Grey
    }
}