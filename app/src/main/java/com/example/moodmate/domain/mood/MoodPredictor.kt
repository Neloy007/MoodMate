package com.example.moodmate.domain.mood

import com.example.moodmate.data.local.MoodEntity
import com.example.moodmate.domain.model.Mood
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoodPredictor @Inject constructor() {

    private val recentMoods = mutableListOf<MoodEntity>()
    private val windowSize = 30 // Last 30 entries

    fun addMood(mood: MoodEntity) {
        recentMoods.add(mood)
        if (recentMoods.size > windowSize) {
            recentMoods.removeAt(0)
        }
    }

    fun predictNextMood(): MoodPrediction {
        if (recentMoods.size < 5) {
            return MoodPrediction(
                predictedMood = Mood.UNKNOWN,
                confidence = 0f,
                message = "Need more data to predict"
            )
        }

        // Simple pattern detection
        val moodPattern = recentMoods.map { it.mood }
        val predictions = mutableMapOf<Mood, Float>()

        // Method 1: Most recent trend
        val recentTrend = moodPattern.takeLast(5)
        val recentCounts = recentTrend.groupingBy { it }.eachCount()
        recentCounts.forEach { (mood, count) ->
            predictions[mood] = (predictions[mood] ?: 0f) + (count / 5f) * 0.6f
        }

        // Method 2: Time-based patterns (same time of day)
        val currentHour = LocalDateTime.now().hour
        val sameTimeMoods = recentMoods.filter {
            val hour = LocalDateTime.ofInstant(
                java.time.Instant.ofEpochMilli(it.createdAt),
                ZoneId.systemDefault()
            ).hour
            hour in (currentHour - 2)..(currentHour + 2)
        }

        if (sameTimeMoods.isNotEmpty()) {
            val timeCounts = sameTimeMoods.groupingBy { it.mood }.eachCount()
            val total = sameTimeMoods.size.toFloat()
            timeCounts.forEach { (mood, count) ->
                predictions[mood] = (predictions[mood] ?: 0f) + (count / total) * 0.4f
            }
        }

        // Get best prediction
        val bestPrediction = predictions.maxByOrNull { it.value }

        return if (bestPrediction != null && bestPrediction.value > 0.3f) {
            MoodPrediction(
                predictedMood = bestPrediction.key,
                confidence = bestPrediction.value,
                message = when (bestPrediction.key) {
                    Mood.HAPPY -> "Looks like you might be happy today!"
                    Mood.NEUTRAL -> "You might feel neutral today"
                    Mood.TIRED -> "You might be tired, take some rest"
                    Mood.UNKNOWN -> "Can't predict accurately"
                }
            )
        } else {
            MoodPrediction(
                predictedMood = Mood.UNKNOWN,
                confidence = 0f,
                message = "No clear pattern detected"
            )
        }
    }

    fun getMoodPatterns(): List<MoodPattern> {
        if (recentMoods.size < 10) return emptyList()

        val patterns = mutableListOf<MoodPattern>()

        // Analyze patterns based on time of day
        val timeSlots = listOf(
            "Morning" to (6..11),
            "Afternoon" to (12..16),
            "Evening" to (17..20),
            "Night" to listOf(21, 22, 23, 0, 1, 2, 3, 4, 5)
        )

        timeSlots.forEach { (slotName, hours) ->
            val slotMoods = recentMoods.filter {
                val hour = LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochMilli(it.createdAt),
                    ZoneId.systemDefault()
                ).hour
                when (hours) {
                    is IntRange -> hour in hours
                    is List<*> -> hour in hours
                    else -> false
                }
            }

            if (slotMoods.isNotEmpty()) {
                val moodCounts = slotMoods.groupingBy { it.mood }.eachCount()
                val dominantMood = moodCounts.maxByOrNull { it.value }?.key
                dominantMood?.let {
                    patterns.add(
                        MoodPattern(
                            timeSlot = slotName,
                            dominantMood = it,
                            frequency = moodCounts[it]?.toFloat()?.div(slotMoods.size) ?: 0f
                        )
                    )
                }
            }
        }

        return patterns
    }
}

data class MoodPrediction(
    val predictedMood: Mood,
    val confidence: Float,
    val message: String
)

data class MoodPattern(
    val timeSlot: String,
    val dominantMood: Mood,
    val frequency: Float
)