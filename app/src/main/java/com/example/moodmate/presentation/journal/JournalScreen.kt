package com.example.moodmate.presentation.journal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moodmate.domain.model.Mood
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun JournalScreen(
    moodId: Int,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    viewModel: JournalViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Journal Entry",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "How are you feeling?",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Mood selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MoodButton(
                        mood = Mood.HAPPY,
                        emoji = "😊",
                        isSelected = state.selectedMood == Mood.HAPPY,
                        onClick = { viewModel.selectMood(Mood.HAPPY) }
                    )
                    MoodButton(
                        mood = Mood.NEUTRAL,
                        emoji = "😐",
                        isSelected = state.selectedMood == Mood.NEUTRAL,
                        onClick = { viewModel.selectMood(Mood.NEUTRAL) }
                    )
                    MoodButton(
                        mood = Mood.TIRED,
                        emoji = "😴",
                        isSelected = state.selectedMood == Mood.TIRED,
                        onClick = { viewModel.selectMood(Mood.TIRED) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Write your thoughts",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.note,
                    onValueChange = viewModel::updateNote,
                    placeholder = { Text("What's on your mind?") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 8,
                    maxLines = 15
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.outlinedButtonColors()
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    viewModel.saveJournal()
                    onSave()
                },
                enabled = state.selectedMood != null && state.note.isNotBlank()
            ) {
                Text("Save Journal")
            }
        }
    }
}

@Composable
fun MoodButton(
    mood: Mood,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = if (isSelected) {
            ButtonDefaults.buttonColors()
        } else {
            ButtonDefaults.outlinedButtonColors()
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = mood.name,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}