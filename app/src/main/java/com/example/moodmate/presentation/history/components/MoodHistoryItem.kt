package com.example.moodmate.presentation.history.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moodmate.data.local.MoodEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MoodHistoryItem(
    mood: MoodEntity,
    onDelete: () -> Unit
) {

    val date = SimpleDateFormat(
        "dd MMM yyyy  hh:mm a",
        Locale.getDefault()
    ).format(Date(mood.createdAt))

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = mood.mood.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Smile: ${(mood.smileProbability * 100).toInt()}%"
                )

                Text(date)

            }

            IconButton(
                onClick = onDelete
            ) {

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )

            }

        }

    }

}