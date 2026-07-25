package com.example.moodmate.presentation.notifications

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.moodmate.domain.repository.MoodRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class MoodReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val notificationHelper: NotificationHelper,
    private val moodRepository: MoodRepository
) : CoroutineWorker(context, params) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        return try {
            // Check if user has checked mood today
            val moods = moodRepository.getMoodHistory()
            val todayMoodCount = moods.firstOrNull()
                ?.filter {
                    val today = getTodayTimestamp()
                    it.createdAt >= today
                }
                ?.size ?: 0

            // Send notification if no mood checked today
            if (todayMoodCount == 0) {
                notificationHelper.showMoodReminder()
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun getTodayTimestamp(): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}