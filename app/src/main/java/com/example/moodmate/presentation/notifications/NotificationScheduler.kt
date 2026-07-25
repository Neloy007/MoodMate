package com.example.moodmate.presentation.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun scheduleDailyReminder() {
        val workRequest = PeriodicWorkRequest.Builder(
            MoodReminderWorker::class.java,
            1, TimeUnit.DAYS
        ).apply {
            // Set initial delay to morning
            val now = System.currentTimeMillis()
            val morningTime = getMorningTimestamp()
            val initialDelay = if (now < morningTime) {
                morningTime - now
            } else {
                morningTime + TimeUnit.DAYS.toMillis(1) - now
            }
            setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        }.build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "mood_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    fun scheduleWeeklyReport() {
        val workRequest = PeriodicWorkRequest.Builder(
            MoodReminderWorker::class.java,
            7, TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "weekly_report",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    fun cancelReminders() {
        WorkManager.getInstance(context).cancelUniqueWork("mood_reminder")
        WorkManager.getInstance(context).cancelUniqueWork("weekly_report")
    }

    private fun getMorningTimestamp(): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 9)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}