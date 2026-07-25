package com.example.moodmate.presentation.analytics

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moodmate.domain.model.Mood
import com.example.moodmate.utils.CSVExporter
import kotlinx.coroutines.launch

@Composable
fun AnalyticsScreen(
    modifier: Modifier = Modifier,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    val moodsByDay by viewModel.moodsByDay.collectAsStateWithLifecycle()
    val last7DaysTrend by viewModel.last7DaysTrend.collectAsStateWithLifecycle()
    val allMoods by viewModel.allMoods.collectAsStateWithLifecycle()

    val csvExporter = CSVExporter(context)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF5F8FF),
                        Color.White
                    )
                )
            )
            .padding(16.dp)
    ) {
        // Header with export button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "📊 Analytics",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A2E)
                )
            )

            if (allMoods.isNotEmpty()) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = Color(0xFF4A90D9).copy(alpha = 0.1f),
                    onClick = {
                        scope.launch {
                            csvExporter.exportMoodsToCSV(
                                moods = allMoods,
                                onSuccess = { file ->
                                    Toast.makeText(
                                        context,
                                        "Exported to: ${file.absolutePath}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    csvExporter.shareCSV(context, file)
                                },
                                onError = { error ->
                                    Toast.makeText(
                                        context,
                                        "Export failed: $error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Export Data",
                            tint = Color(0xFF4A90D9)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle
        Text(
            text = "Track your emotional patterns and insights",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF666666)
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Stats Cards
        StatsRow(stats = stats)

        Spacer(modifier = Modifier.height(20.dp))

        // Mood Distribution Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mood Distribution",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1A1A2E)
                        )
                    )
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF4A90D9).copy(alpha = 0.1f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("📊", fontSize = 16.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (stats.totalMoods == 0) {
                    Text(
                        text = "No data available yet. Start tracking your mood!",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF888888),
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )
                } else {
                    stats.moodDistribution.forEach { (mood, count) ->
                        if (count > 0) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${getMoodEmoji(Mood.valueOf(mood))} $mood",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF1A1A2E)
                                        )
                                    )
                                    Text(
                                        text = "$count (${(count / stats.totalMoods.toFloat() * 100).toInt()}%)",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color(0xFF666666)
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                LinearProgressIndicator(
                                    progress = count / stats.totalMoods.toFloat(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = getMoodColor(Mood.valueOf(mood)),
                                    trackColor = Color(0xFFF0F0F0)
                                )

                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recent Trend Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Last 7 Days Trend",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1A1A2E)
                        )
                    )
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF4A90D9).copy(alpha = 0.1f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("📈", fontSize = 16.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (last7DaysTrend.isEmpty()) {
                    Text(
                        text = "No data in the last 7 days",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF888888),
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        last7DaysTrend.takeLast(7).forEachIndexed { index, mood ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Day label
                                Text(
                                    text = getDayLabel(index, last7DaysTrend.size),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF888888)
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                // Mood emoji with background
                                Surface(
                                    modifier = Modifier.size(48.dp),
                                    shape = CircleShape,
                                    color = getMoodColor(mood).copy(alpha = 0.15f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = getMoodEmoji(mood),
                                            style = MaterialTheme.typography.headlineSmall
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = mood.name.take(3),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF666666)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Total entries info
        if (allMoods.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4A90D9).copy(alpha = 0.08f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📁",
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${allMoods.size} entries available for export",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF4A90D9),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun StatsRow(stats: MoodAnalyticsStats) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatItem(
            label = "Total",
            value = stats.totalMoods.toString(),
            icon = "📊",
            color = Color(0xFF4A90D9),
            modifier = Modifier.weight(1f)  // Fixed: weight is a modifier function
        )
        StatItem(
            label = "Most Common",
            value = stats.mostCommonMood,
            icon = "🏆",
            color = Color(0xFFFF9800),
            modifier = Modifier.weight(1f)  // Fixed: weight is a modifier function
        )
        StatItem(
            label = "Today",
            value = stats.todayMoodCount.toString(),
            icon = "📅",
            color = Color(0xFF4CAF50),
            modifier = Modifier.weight(1f)  // Fixed: weight is a modifier function
        )
        StatItem(
            label = "Avg Smile",
            value = "${(stats.averageSmileProbability * 100).toInt()}%",
            icon = "😊",
            color = Color(0xFF9C27B0),
            modifier = Modifier.weight(1f)  // Fixed: weight is a modifier function
        )
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier  // Added modifier parameter
) {
    Card(
        modifier = modifier,  // Use the modifier parameter
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFF888888)
                )
            )
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

fun getDayLabel(index: Int, total: Int): String {
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val startIndex = (total - 7).coerceAtLeast(0)
    val dayIndex = (startIndex + index) % 7
    return days[dayIndex]
}