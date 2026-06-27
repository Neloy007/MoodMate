package com.example.moodmate.domain.mood

import com.example.moodmate.domain.model.Mood

class MoodStabilizer(
    private val windowSize: Int = 10
) {

    private val moodHistory = ArrayDeque<Mood>()

    fun addMood(mood: Mood): Mood {

        moodHistory.addLast(mood)

        if (moodHistory.size > windowSize) {
            moodHistory.removeFirst()
        }

        val moodCount = moodHistory.groupingBy { it }.eachCount()

        return moodCount.maxByOrNull { it.value }?.key ?: Mood.UNKNOWN
    }
}