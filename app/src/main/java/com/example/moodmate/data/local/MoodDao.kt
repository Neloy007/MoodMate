package com.example.moodmate.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(
        mood: MoodEntity
    )

    @Delete
    suspend fun deleteMood(
        mood: MoodEntity
    )

    @Query("SELECT * FROM moods ORDER BY createdAt DESC")
    fun getAllMoods(): Flow<List<MoodEntity>>

    @Query("DELETE FROM moods")
    suspend fun deleteAll()
}