package com.example.moodmate.presentation.camera.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodmate.domain.model.Mood

@Composable
fun MoodInsightCard(
    mood: Mood,
    confidence: Float,
    modifier: Modifier = Modifier
) {
    val moodData = getMoodData(mood, confidence)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A2E).copy(alpha = 0.85f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header: Mood Emoji + Name + Confidence
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Mood Emoji in circle
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = getMoodColor(mood).copy(alpha = 0.2f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = getMoodEmoji(mood),
                                fontSize = 24.sp
                            )
                        }
                    }

                    Column {
                        Text(
                            text = moodData.displayName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Confidence: ${(confidence * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = getMoodColor(mood)
                            )
                        )
                    }
                }

                // Confidence bar
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "${(confidence * 100).toInt()}%",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = getMoodColor(mood)
                        )
                    )
                    LinearProgressIndicator(
                        progress = confidence,
                        modifier = Modifier
                            .width(60.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = getMoodColor(mood),
                        trackColor = Color.White.copy(alpha = 0.1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Insight Text
            Text(
                text = moodData.insight,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.8f),
                    lineHeight = 22.sp
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Divider
            Divider(
                color = Color.White.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Suggestions Section
            Column {
                Text(
                    text = "💡 Suggestions for You",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                moodData.suggestions.forEach { suggestion ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "• ",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = getMoodColor(mood)
                            )
                        )
                        Text(
                            text = suggestion,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.7f),
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Positive Affirmation
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = getMoodColor(mood).copy(alpha = 0.1f)
            ) {
                Text(
                    text = "✨ ${getAffirmation()}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Daily Self-Care Challenge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🌱 Daily Challenge",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                )
                Text(
                    text = getDailyChallenge(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = getMoodColor(mood)
                    )
                )
            }
        }
    }
}

data class MoodData(
    val displayName: String,
    val insight: String,
    val suggestions: List<String>
)

fun getMoodData(mood: Mood, confidence: Float): MoodData {
    return when (mood) {
        Mood.HAPPY -> MoodData(
            displayName = "Happy 😊",
            insight = "Your smile and relaxed facial expression suggest a positive mood. Remember that facial expressions aren't always a perfect reflection of how someone feels, so feel free to adjust your mood manually if it doesn't match how you're feeling.",
            suggestions = listOf(
                "Celebrate a small win today.",
                "Share your positivity with someone.",
                "Take a photo or write down what made today special.",
                "Stay active with a short walk or workout.",
                "Practice gratitude by listing three good things today."
            )
        )
        Mood.NEUTRAL -> MoodData(
            displayName = "Neutral 😐",
            insight = "Your neutral expression suggests you're in a balanced state. This is a great opportunity to check in with yourself and decide what you need right now.",
            suggestions = listOf(
                "Take a 10-minute walk outside.",
                "Listen to your favorite music.",
                "Drink a glass of water and stretch.",
                "Try a new hobby or activity.",
                "Write one thing you're looking forward to today."
            )
        )
        Mood.TIRED -> MoodData(
            displayName = "Tired 😴",
            insight = "Your facial expression shows signs of fatigue. Your body and mind might need some rest and rejuvenation. Remember to take care of yourself.",
            suggestions = listOf(
                "Drink water and take a short break.",
                "Rest your eyes for a few minutes.",
                "Have a healthy snack.",
                "If possible, take a 20-minute power nap.",
                "Try to get enough sleep tonight."
            )
        )
        Mood.UNKNOWN -> MoodData(
            displayName = "Not Detected ❓",
            insight = "We couldn't detect your mood from your facial expression. You can manually select your mood below to continue tracking.",
            suggestions = listOf(
                "Adjust your lighting and try again.",
                "Make sure your face is clearly visible.",
                "Try looking directly at the camera.",
                "You can also select your mood manually below."
            )
        )
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
        Mood.HAPPY -> Color(0xFF4CAF50)
        Mood.NEUTRAL -> Color(0xFFFF9800)
        Mood.TIRED -> Color(0xFF9C27B0)
        Mood.UNKNOWN -> Color(0xFF9E9E9E)
    }
}

fun getAffirmation(): String {
    val affirmations = listOf(
        "Every day is a new opportunity.",
        "Your feelings are valid.",
        "Small steps lead to big changes.",
        "Take one moment at a time.",
        "You are stronger than you think.",
        "Progress is more important than perfection.",
        "You are capable of amazing things.",
        "This too shall pass.",
        "You deserve kindness, especially from yourself.",
        "Every challenge is a chance to grow."
    )
    return affirmations.random()
}

fun getDailyChallenge(): String {
    val challenges = listOf(
        "Drink 8 glasses of water 💧",
        "Walk for 15 minutes 🚶",
        "Spend 10 minutes without your phone 📱",
        "Write three things you're grateful for ✍️",
        "Meditate for 5 minutes 🧘",
        "Read a book for 15 minutes 📖",
        "Go to bed 30 minutes earlier 🌙",
        "Call a friend or family member 📞",
        "Practice deep breathing for 2 minutes 🌬️",
        "Do 10 minutes of stretching 🤸"
    )
    return challenges.random()
}