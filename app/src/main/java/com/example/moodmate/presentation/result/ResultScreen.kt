package com.example.moodmate.presentation.result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moodmate.R
import com.example.moodmate.domain.model.Mood

@Composable
fun ResultScreen(
    mood: Mood,
    smileProbability: Float,
    onDone: () -> Unit = {},
    viewModel: ResultViewModel = hiltViewModel()
) {
    var note by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val isSaved by viewModel.isSaved.collectAsStateWithLifecycle()

    LaunchedEffect(isSaved) {
        if (isSaved) {
            showSuccessDialog = true
        }
    }

    fun handleSaveMood() {
        isSaving = true
        viewModel.saveMoodWithNote(
            mood = mood,
            smileProbability = smileProbability,
            note = note
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.backgroundresult),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.15f
        )

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "Capture Your Thoughts",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A2E),
                    fontSize = 28.sp
                ),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Your mood matters. Your thoughts matter too.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF888888)
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Divider
            Divider(
                color = Color(0xFFE8E8E8),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Mood Selected Card - Exactly as design
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left side: Emoji + Mood Name
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Mood Emoji in circle
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = getMoodColor(mood).copy(alpha = 0.15f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = getMoodEmoji(mood),
                                    fontSize = 22.sp
                                )
                            }
                        }
                        Text(
                            text = mood.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1A2E)
                            )
                        )
                    }

                    // Right side: Mood Selected Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF4A90D9).copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = "Mood Selected 🎉",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF4A90D9)
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Note Input Section - with proper alignment
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Add a Note (Optional)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A1A2E)
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Tell us what's on your mind. A few words today can help you understand your emotions tomorrow.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF888888)
                    ),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Text Field
                OutlinedTextField(
                    value = note,
                    onValueChange = {
                        if (it.length <= 500) {
                            note = it
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    placeholder = {
                        Text(
                            text = "Write about your thoughts, feelings, or what happened today...",
                            color = Color(0xFFBDBDBD),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4A90D9),
                        unfocusedBorderColor = Color(0xFFD0D0D0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text
                    ),
                    minLines = 4,
                    maxLines = 6
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Character Counter - aligned to end
                Text(
                    text = "${note.length}/500",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (note.length > 450) Color(0xFFFF5252) else Color(0xFFBDBDBD)
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save Mood Button
            Button(
                onClick = { handleSaveMood() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90D9)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp
                ),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Save Mood",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )
                }
            }
        }

        // Success Dialog
        if (showSuccessDialog) {
            SuccessDialog(
                onContinue = {
                    showSuccessDialog = false
                    onDone()
                }
            )
        }
    }
}

@Composable
fun SuccessDialog(
    onContinue: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success Emoji
                Text(
                    text = "🎉",
                    fontSize = 56.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Mood Saved! 🎉",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A2E)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your mood and personal note have been successfully recorded. You can revisit them anytime in your journal.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF666666),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A90D9)
                    )
                ) {
                    Text(
                        text = "Continue",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

fun getMoodEmoji(mood: Mood): String {
    return when (mood) {
        Mood.HAPPY -> "😊"
        Mood.NEUTRAL -> "😐"
        Mood.TIRED -> "😴"
        Mood.UNKNOWN -> "❓"
    }
}

fun getMoodColor(mood: Mood): Color {
    return when (mood) {
        Mood.HAPPY -> Color(0xFF4CAF50)
        Mood.NEUTRAL -> Color(0xFFFF9800)
        Mood.TIRED -> Color(0xFF9C27B0)
        Mood.UNKNOWN -> Color(0xFF9E9E9E)
    }
}