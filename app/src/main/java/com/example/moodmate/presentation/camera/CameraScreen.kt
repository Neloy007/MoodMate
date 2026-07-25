package com.example.moodmate.presentation.camera

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moodmate.R
import com.example.moodmate.domain.model.Mood
import com.example.moodmate.domain.mood.FaceQuality
import com.example.moodmate.presentation.camera.components.InstantStyleCameraView
import com.example.moodmate.presentation.camera.components.MoodInfoCard
import com.example.moodmate.presentation.camera.components.MoodInsightCard
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    onSaved: (Mood, Float) -> Unit,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val viewModel: CameraViewModel = hiltViewModel()

    val faceResult by viewModel.faceResult.collectAsStateWithLifecycle()
    val mood by viewModel.mood.collectAsStateWithLifecycle()
    val faceQuality by viewModel.faceQuality.collectAsStateWithLifecycle()

    // Calculate confidence
    val confidence = when {
        faceResult.faceCount == 0 -> 0f
        faceResult.smileProbability != null -> faceResult.smileProbability ?: 0f
        else -> 0.5f
    }

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    when {
        permissionState.status.isGranted -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0A0A1A),
                                Color(0xFF1A1A2E)
                            )
                        )
                    )
            ) {
                // Background subtle pattern
                Image(
                    painter = painterResource(id = R.drawable.backgroundresult),
                    contentDescription = "Background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.05f
                )

                // Main Content - Scrollable
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header
                    Text(
                        text = "Check Mood",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 26.sp
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "How are you feeling right now?",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Divider
                    Divider(
                        color = Color.White.copy(alpha = 0.1f),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Camera Preview
                    InstantStyleCameraView(
                        onFacesDetected = viewModel::updateFaceResult,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        showHoldText = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mood Insight Card or Detection State
                    if (mood != Mood.UNKNOWN && faceQuality == FaceQuality.Good) {
                        MoodInsightCard(
                            mood = mood,
                            confidence = confidence,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        // Detection waiting state
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF1A1A2E).copy(alpha = 0.85f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = when (faceQuality) {
                                        FaceQuality.NoFace -> "📷 Position Your Face"
                                        FaceQuality.MultipleFaces -> "👥 Only One Face Please"
                                        FaceQuality.EyesClosed -> "👀 Open Your Eyes"
                                        FaceQuality.Good -> "🔍 Detecting Your Mood..."
                                    },
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = when (faceQuality) {
                                        FaceQuality.NoFace -> "Please look directly at the camera"
                                        FaceQuality.MultipleFaces -> "Make sure only one face is visible"
                                        FaceQuality.EyesClosed -> "Keep your eyes open for detection"
                                        FaceQuality.Good -> "Almost ready..."
                                    },
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.White.copy(alpha = 0.6f),
                                        textAlign = TextAlign.Center
                                    )
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                LinearProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp),
                                    color = Color(0xFF4A90D9),
                                    trackColor = Color.White.copy(alpha = 0.1f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Manual Mood Selection
                    Text(
                        text = "Or select your mood manually",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.White.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MoodOptionButton(
                            emoji = "😊",
                            label = "Happy",
                            isSelected = mood == Mood.HAPPY,
                            onClick = { /* Optional manual selection */ }
                        )
                        MoodOptionButton(
                            emoji = "😐",
                            label = "Neutral",
                            isSelected = mood == Mood.NEUTRAL,
                            onClick = { /* Optional manual selection */ }
                        )
                        MoodOptionButton(
                            emoji = "😴",
                            label = "Tired",
                            isSelected = mood == Mood.TIRED,
                            onClick = { /* Optional manual selection */ }
                        )
                        MoodOptionButton(
                            emoji = "😔",
                            label = "Sad",
                            isSelected = mood == Mood.UNKNOWN,
                            onClick = { /* Optional manual selection */ }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Save Mood Button
                    Button(
                        onClick = {
                            viewModel.saveMood()
                            onSaved(mood, confidence)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4A90D9)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp
                        ),
                        enabled = mood != Mood.UNKNOWN && faceQuality == FaceQuality.Good
                    ) {
                        Text(
                            text = "Save Mood",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Cancel button
                    TextButton(
                        onClick = onBack,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Cancel",
                            color = Color.White.copy(alpha = 0.4f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Back button (top left)
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .size(40.dp)
                        .clip(CircleShape),
                    color = Color.White.copy(alpha = 0.08f),
                    onClick = onBack
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        permissionState.status.shouldShowRationale -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0A0A1A),
                                Color(0xFF1A1A2E)
                            )
                        )
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = Color(0xFF4A90D9).copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "📷", fontSize = 40.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Camera Permission",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "We need camera access to detect your mood from facial expressions. This helps us provide you with personalized mood insights.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { permissionState.launchPermissionRequest() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A90D9)
                    )
                ) {
                    Text(
                        text = "Grant Permission",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Go Back",
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
            }
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0A0A1A),
                                Color(0xFF1A1A2E)
                            )
                        )
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = Color(0xFFFF5252).copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "⛔", fontSize = 40.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Permission Denied",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Camera permission has been permanently denied. Please enable it in your device settings to use this feature.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A90D9)
                    )
                ) {
                    Text(
                        text = "Open Settings",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Go Back",
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun MoodOptionButton(
    emoji: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape),
            color = if (isSelected) {
                Color(0xFF4A90D9)
            } else {
                Color(0xFF1A1A2E).copy(alpha = 0.5f)
            },
            onClick = onClick
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = emoji, fontSize = 22.sp)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.4f)
            )
        )
    }
}