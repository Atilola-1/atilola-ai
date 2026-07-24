package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.BuildConfig
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.SoftGold
import com.example.ui.theme.CardCharcoal
import com.example.viewmodel.MainViewModel

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    val isDarkTheme by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val speedValue by viewModel.ttsSpeed.collectAsStateWithLifecycle()
    val defaultLang by viewModel.ttsLanguage.collectAsStateWithLifecycle()
    val notifyEnabled by viewModel.notificationEnabled.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    // Read BuildConfig to determine API key status
    val hasApiKey = remember {
        val key = BuildConfig.GEMINI_API_KEY
        key.isNotEmpty() && key != "MY_GEMINI_API_KEY"
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "APPLICATION CONTROL",
                    fontSize = 12.sp,
                    color = RoyalGold,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Settings & AI Panel",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // User Session Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(RoyalGold.copy(alpha = 0.1f), RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = RoyalGold)
                        }

                        Column {
                            Text(
                                text = currentUser?.name ?: "Guest Reader",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = currentUser?.email ?: "local@atilola.ai",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.logout() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("LOGOUT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Connection & AI Engine Status Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (hasApiKey) RoyalGold.copy(alpha = 0.3f) else MaterialTheme.colorScheme.error.copy(alpha = 0.3f),
                        RoundedCornerShape(16.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ATILOLA AI Core Engine",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        Text(
                            text = if (hasApiKey) "ACTIVE AI" else "OFFLINE MODE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .background(if (hasApiKey) RoyalGold else Color.Gray, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = if (hasApiKey) {
                            "Gemini API model 'gemini-3.5-flash' is successfully active. Tap document words to perform real-time contextual translations and linguistic analyses."
                        } else {
                            "Using offline high-fidelity dictionary dictionaries. To activate real-time Gemini AI, configure GEMINI_API_KEY inside the Secrets Panel of AI Studio."
                        },
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Setting sections
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "App Interface Settings",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalGold,
                    fontFamily = FontFamily.Serif
                )

                // Dark mode toggle row
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(Icons.Default.DarkMode, contentDescription = null, tint = RoyalGold)
                            Text("Luxury Black Theme", fontSize = 14.sp)
                        }

                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { viewModel.isDarkMode.value = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = RoyalGold,
                                checkedTrackColor = RoyalGold.copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                // Push Notification settings
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = RoyalGold)
                            Text("Vocabulary & Study Reminders", fontSize = 14.sp)
                        }

                        Switch(
                            checked = notifyEnabled,
                            onCheckedChange = { viewModel.notificationEnabled.value = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = RoyalGold,
                                checkedTrackColor = RoyalGold.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            }

            // Reading voice speed settings
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Speech Synthesis Controls",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalGold,
                    fontFamily = FontFamily.Serif
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Icon(Icons.Default.Speed, contentDescription = null, tint = RoyalGold)
                                Text("Speech Reading Velocity", fontSize = 14.sp)
                            }
                            Text(
                                text = "${String.format("%.1f", speedValue)}x",
                                fontWeight = FontWeight.Bold,
                                color = RoyalGold
                            )
                        }

                        Slider(
                            value = speedValue,
                            onValueChange = { viewModel.ttsSpeed.value = it },
                            valueRange = 0.5f..2.0f,
                            colors = SliderDefaults.colors(
                                thumbColor = RoyalGold,
                                activeTrackColor = RoyalGold,
                                inactiveTrackColor = Color.Gray.copy(alpha = 0.3f)
                            )
                        )
                    }
                }

                // Default reading translation language dropdown mockup
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(Icons.Default.Translate, contentDescription = null, tint = RoyalGold)
                            Text("Default Translation Language", fontSize = 14.sp)
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf("English", "Hausa", "Yoruba", "Arabic").forEach { lang ->
                                Button(
                                    onClick = { viewModel.ttsLanguage.value = lang },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (defaultLang == lang) RoyalGold else MaterialTheme.colorScheme.background,
                                        contentColor = if (defaultLang == lang) Color.Black else MaterialTheme.colorScheme.onSurface
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(2.dp)
                                ) {
                                    Text(lang, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // AI Studio configuration guidelines
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Linguistic AI Setup Instructions",
                        fontWeight = FontWeight.Bold,
                        color = RoyalGold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = "1. Locate and access the SECRETS panel inside your Google AI Studio project sidebar.\n" +
                                "2. Declare a secure secret parameter named GEMINI_API_KEY.\n" +
                                "3. Transmit your personal API key credential into the field value.\n" +
                                "4. The secure compiler automatically injects this value during subsequent application operations, activating full real-time contextual definitions.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}
