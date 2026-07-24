package com.example.moodmate.presentation.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moodmate.presentation.history.components.MoodHistoryItem

@Composable
fun HistoryScreen() {

    val viewModel: HistoryViewModel = hiltViewModel()

    val moods by viewModel.moods.collectAsStateWithLifecycle()

    if (moods.isEmpty()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "No moods recorded yet.",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(24.dp)
            )

        }

    } else {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(moods) { mood ->

                MoodHistoryItem(
                    mood = mood,
                    onDelete = {
                        viewModel.deleteMood(mood)
                    }
                )

            }

        }

    }

}