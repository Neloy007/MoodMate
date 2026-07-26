package com.example.moodmate.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodmate.R
import java.util.*

// Palette pulled from the design
private val BgLavender = Color(0xFFF3F1FB)
private val PurplePrimary = Color(0xFF7C6FE0)
private val PurpleDark = Color(0xFF1A1A2E)
private val TextGray = Color(0xFF8A8A9A)
private val CardWhite = Color(0xFFFFFFFF)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    userName: String = "Neloy",
    onCheckMoodClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onAnalyticsClick: () -> Unit = {},
    onJournalClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val timeOfDay = when (hour) {
        in 0..11 -> "Morning"
        in 12..16 -> "Afternoon"
        else -> "Evening"
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Background image - shown at full strength, no lavender wash on top of it
        Image(
            painter = painterResource(id = R.drawable.bgimagehome),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Top bar: hamburger + notification bell
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoundIconButton(icon = Icons.Outlined.Menu, onClick = onMenuClick)
                Box {
                    RoundIconButton(icon = Icons.Outlined.Notifications, onClick = onNotificationsClick)
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .align(Alignment.TopEnd)
                            .background(PurplePrimary, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Greeting + illustration
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Good $timeOfDay,",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextGray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$userName 👋",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PurplePrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "How are you today?",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PurpleDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Your mood matters. Let's\ncheck in and take care of you.",
                        fontSize = 15.sp,
                        color = TextGray,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(15.dp))

            }

            Spacer(modifier = Modifier.height(28.dp))

            // Check Mood banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF8B7EE8), Color(0xFF6C5CE7))
                        )
                    )
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🙂", fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Check Mood",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "How are you feeling right now?",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }

                    IconButton(
                        onClick = onCheckMoodClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Check mood",
                            tint = PurplePrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2x2 grid
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    MenuItemCard(
                        icon = Icons.Default.MenuBook,
                        iconBg = Color(0xFFEDE9FE),
                        iconTint = PurplePrimary,
                        title = "Journal",
                        subtitle = "View your mood history & notes",
                        onClick = onJournalClick,
                        modifier = Modifier.weight(1f)
                    )
                    MenuItemCard(
                        icon = Icons.Default.BarChart,
                        iconBg = Color(0xFFDDF5E8),
                        iconTint = Color(0xFF34B36B),
                        title = "Analytics",
                        subtitle = "See your mood patterns & insights",
                        onClick = onAnalyticsClick,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    MenuItemCard(
                        icon = Icons.Default.CalendarMonth,
                        iconBg = Color(0xFFFCE4EC),
                        iconTint = Color(0xFFE0568A),
                        title = "History",
                        subtitle = "Browse your past mood records",
                        onClick = onHistoryClick,
                        modifier = Modifier.weight(1f)
                    )
                    MenuItemCard(
                        icon = Icons.Default.Settings,
                        iconBg = Color(0xFFE3EEFC),
                        iconTint = Color(0xFF4A90D9),
                        title = "Settings",
                        subtitle = "Customize your experience",
                        onClick = onSettingsClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

    }
}

@Composable
private fun RoundIconButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(imageVector = icon, contentDescription = null, tint = PurpleDark)
        }
    }
}

@Composable
fun MenuItemCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconTint)
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = PurplePrimary
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleDark
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextGray,
                    lineHeight = 16.sp,
                    maxLines = 2
                )
            }
        }
    }
}