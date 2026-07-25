package com.example.moodmate.di

import android.content.Context
import androidx.room.Room
import com.example.moodmate.data.local.MIGRATION_1_2
import com.example.moodmate.data.local.MoodDao
import com.example.moodmate.data.local.MoodDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MoodDatabase {
        return Room.databaseBuilder(
            context,
            MoodDatabase::class.java,
            "mood_database"
        ).addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoodDao(
        database: MoodDatabase
    ): MoodDao {

        return database.moodDao()
    }
}