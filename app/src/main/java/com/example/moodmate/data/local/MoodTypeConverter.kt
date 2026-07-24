package com.example.moodmate.data.local

import androidx.room.TypeConverter
import com.example.moodmate.domain.model.Mood

class MoodTypeConverter {

    @TypeConverter
    fun fromMood(
        mood: Mood
    ): String = mood.name

    @TypeConverter
    fun toMood(
        value: String
    ): Mood = Mood.valueOf(value)
}