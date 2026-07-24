package com.example.moodmate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [MoodEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(MoodTypeConverter::class)
abstract class MoodDatabase : RoomDatabase() {

    abstract fun moodDao(): MoodDao
}