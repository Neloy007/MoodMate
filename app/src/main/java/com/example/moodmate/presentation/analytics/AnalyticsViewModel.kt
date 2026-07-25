package com.example.moodmate.presentation.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodmate.data.local.MoodEntity
import com.example.moodmate.domain.model.Mood
import com.example.moodmate.domain.usecase.GetMoodHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getMoodHistoryUseCase: GetMoodHistoryUseCase
) : ViewModel() {

    // All moods from database - make this accessible for export
    val allMoods = getMoodHistoryUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    // Total count
    val totalMoods = allMoods.map { it.size }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    // Mood distribution
    val moodDistribution = allMoods.map { moods ->
        moods.groupingBy { it.mood }
            .eachCount()
            .mapKeys { it.key.name }
            .toMutableMap()
            .apply {
                // Ensure all moods are present even if count is 0
                Mood.entries.forEach { mood ->
                    putIfAbsent(mood.name, 0)
                }
            }
            .toMap()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        mapOf()
    )

    // Most common mood
    val mostCommonMood = moodDistribution.map { distribution ->
        distribution.maxByOrNull { it.value }?.key ?: "No Data"
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "No Data"
    )

    // Average smile probability
    val averageSmile = allMoods.map { moods ->
        if (moods.isEmpty()) 0f
        else moods.map { it.smileProbability }.average().toFloat()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0f
    )

    // Moods by day (for chart)
    val moodsByDay = allMoods.map { moods ->
        moods.groupBy {
            formatDate(it.createdAt)
        }.mapValues { entry ->
            entry.value.groupingBy { it.mood }
                .eachCount()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        mapOf()
    )

    // Mood trend (last 7 days)
    val last7DaysTrend = allMoods.map { moods ->
        val sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L)
        moods.filter { it.createdAt >= sevenDaysAgo }
            .sortedBy { it.createdAt }
            .map { it.mood }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // Today's mood count
    val todayMoodCount = allMoods.map { moods ->
        val today = getTodayTimestamp()
        moods.count { it.createdAt >= today }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    // Today's mood summary
    val todayMoods = allMoods.map { moods ->
        val today = getTodayTimestamp()
        moods.filter { it.createdAt >= today }
            .sortedByDescending { it.createdAt }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // Combine stats for display
    val stats = combine(
        totalMoods,
        mostCommonMood,
        averageSmile,
        todayMoodCount,
        moodDistribution
    ) { total, common, avgSmile, today, distribution ->
        MoodAnalyticsStats(
            totalMoods = total,
            mostCommonMood = common,
            averageSmileProbability = avgSmile,
            todayMoodCount = today,
            moodDistribution = distribution
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MoodAnalyticsStats()
    )

    // Get moods for a specific date range
    fun getMoodsInRange(startDate: Long, endDate: Long) = allMoods.map { moods ->
        moods.filter { it.createdAt in startDate..endDate }
    }

    // Get mood percentage
    fun getMoodPercentage(mood: Mood): Float {
        val distribution = moodDistribution.value
        val total = totalMoods.value
        return if (total > 0) (distribution[mood.name] ?: 0) / total.toFloat() else 0f
    }

    private fun formatDate(timestamp: Long): String {
        val date = java.util.Date(timestamp)
        val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return format.format(date)
    }

    private fun getTodayTimestamp(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}

data class MoodAnalyticsStats(
    val totalMoods: Int = 0,
    val mostCommonMood: String = "No Data",
    val averageSmileProbability: Float = 0f,
    val todayMoodCount: Int = 0,
    val moodDistribution: Map<String, Int> = mapOf()
)