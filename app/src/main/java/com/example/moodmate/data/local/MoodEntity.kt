package com.example.moodmate.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moodmate.domain.model.Mood

@Entity(tableName = "moods")
data class MoodEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val mood: Mood,

    val smileProbability: Float,

    val createdAt: Long
)