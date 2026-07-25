package com.example.moodmate

import android.app.Application
import com.example.moodmate.presentation.notifications.NotificationScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MoodMateApp : Application() {

    @Inject
    lateinit var notificationScheduler: NotificationScheduler

    override fun onCreate() {
        super.onCreate()

        // Schedule notifications when app starts
        notificationScheduler.scheduleDailyReminder()
        notificationScheduler.scheduleWeeklyReport()
    }
}