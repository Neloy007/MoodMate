package com.example.moodmate.presentation.camera

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moodmate.domain.model.Mood
import com.example.moodmate.domain.mood.FaceQuality
import com.example.moodmate.presentation.camera.components.InstantStyleCameraView
import com.example.moodmate.presentation.camera.components.MoodInfoCard
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

private val BgLavender = Color(0xFFF3F1FB)
private val PurplePrimary = Color(0xFF7C6FE0)
private val PurpleDark = Color(0xFF1A1A2E)
private val TextGray = Color(0xFF8A8A9A)

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
                    .background(BgLavender)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Header: back button, title + subtitle, quick-action button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        RoundIconButton(icon = Icons.Default.ArrowBack, onClick = onBack)

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Check Mood",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = PurpleDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "How are you feeling right now?",
                                fontSize = 15.sp,
                                color = TextGray,
                                textAlign = TextAlign.Center
                            )
                        }

                        RoundIconButton(icon = Icons.Default.Bolt, onClick = { /* quick instant action */ })
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Step / progress dots
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ProgressDot(active = true)
                        Spacer(modifier = Modifier.width(6.dp))
                        ProgressDot(active = false)
                        Spacer(modifier = Modifier.width(6.dp))
                        ProgressDot(active = false)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Camera preview
                    InstantStyleCameraView(
                        onFacesDetected = viewModel::updateFaceResult,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(420.dp)
                    )

                    // Mood info sheet, pulled up to slightly overlap the camera.
                    // Mood shown here is fully automatic (live face detection).
                    MoodInfoCard(
                        mood = mood,
                        faceResult = faceResult,
                        faceQuality = faceQuality,
                        onSaveClick = {
                            viewModel.saveMood()
                            onSaved(mood, confidence)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-28).dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        permissionState.status.shouldShowRationale -> {
            PermissionMessageScreen(
                emoji = "📷",
                emojiBg = PurplePrimary.copy(alpha = 0.15f),
                title = "Camera Permission",
                message = "We need camera access to detect your mood from facial expressions. This helps us provide you with personalized mood insights.",
                primaryLabel = "Grant Permission",
                onPrimaryClick = { permissionState.launchPermissionRequest() },
                onBack = onBack
            )
        }

        else -> {
            PermissionMessageScreen(
                emoji = "⛔",
                emojiBg = Color(0xFFFF5252).copy(alpha = 0.15f),
                title = "Permission Denied",
                message = "Camera permission has been permanently denied. Please enable it in your device settings to use this feature.",
                primaryLabel = "Open Settings",
                onPrimaryClick = {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                    context.startActivity(intent)
                },
                onBack = onBack
            )
        }
    }
}

@Composable
private fun RoundIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(imageVector = icon, contentDescription = null, tint = PurplePrimary)
        }
    }
}

@Composable
private fun ProgressDot(active: Boolean) {
    Box(
        modifier = Modifier
            .height(6.dp)
            .width(if (active) 28.dp else 16.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(if (active) PurplePrimary else PurplePrimary.copy(alpha = 0.25f))
    )
}

@Composable
private fun PermissionMessageScreen(
    emoji: String,
    emojiBg: Color,
    title: String,
    message: String,
    primaryLabel: String,
    onPrimaryClick: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLavender)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = emojiBg
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = emoji, fontSize = 40.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PurpleDark
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = message,
            fontSize = 16.sp,
            color = TextGray,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onPrimaryClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
        ) {
            Text(
                text = primaryLabel,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Go Back", color = TextGray)
        }
    }
}