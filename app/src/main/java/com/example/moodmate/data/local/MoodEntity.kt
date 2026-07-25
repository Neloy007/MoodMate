package com.example.moodmate.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moodmate.domain.model.Mood

@Entity(tableName = "moods")
data class MoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "mood")
    val mood: Mood,

    @ColumnInfo(name = "smileProbability")
    val smileProbability: Float,

    @ColumnInfo(name = "createdAt")
    val createdAt: Long,

    @ColumnInfo(name = "note")
    val note: String? = null
)