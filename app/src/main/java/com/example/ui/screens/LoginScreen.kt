package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.SoftGold
import com.example.ui.theme.LuxuryBlack
import com.example.ui.theme.CharcoalDark
import com.example.viewmodel.MainViewModel

@Composable
fun LoginScreen(viewModel: MainViewModel) {
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("atilolaqudus0@gmail.com") }
    var password by remember { mutableStateOf("password123") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    var showForgotPassword by remember { mutableStateOf(false) }
    var showFeedbackDialog by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(LuxuryBlack, CharcoalDark, LuxuryBlack)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Luxury decorative background blur spots
        Box(
            modifier = Modifier
                .size(240.dp)
                .offset(x = (-80).dp, y = (-150).dp)
                .blur(80.dp)
                .background(RoyalGold.copy(alpha = 0.15f), shape = RoundedCornerShape(120.dp))
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = 100.dp, y = 180.dp)
                .blur(70.dp)
                .background(SoftGold.copy(alpha = 0.1f), shape = RoundedCornerShape(100.dp))
        )

        // Glassmorphism Card
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp)
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(RoyalGold.copy(alpha = 0.4f), Color.Transparent)
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .testTag("login_card"),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.75f)
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ATILOLA AI BRAND MARK
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            Brush.sweepGradient(listOf(RoyalGold, SoftGold, RoyalGold)),
                            shape = RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "A",
                        fontSize = 32.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "ATILOLA AI",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = RoyalGold,
                    letterSpacing = 2.sp,
                    fontFamily = FontFamily.Serif
                )

                Text(
                    text = "INTELLIGENT READING SUITE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = SoftGold.copy(alpha = 0.8f),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Input Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address", color = SoftGold) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = RoyalGold) },
                    textStyle = TextStyle(color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoyalGold,
                        unfocusedBorderColor = RoyalGold.copy(alpha = 0.4f),
                        cursorColor = RoyalGold
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("username_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Input Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = SoftGold) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = RoyalGold) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = RoyalGold
                            )
                        }
                    },
                    textStyle = TextStyle(color = Color.White),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoyalGold,
                        unfocusedBorderColor = RoyalGold.copy(alpha = 0.4f),
                        cursorColor = RoyalGold
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("password_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = RoyalGold,
                                uncheckedColor = RoyalGold.copy(alpha = 0.5f)
                            )
                        )
                        Text(
                            text = "Remember Me",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            modifier = Modifier.clickable { rememberMe = !rememberMe }
                        )
                    }

                    Text(
                        text = "Forgot Password?",
                        color = RoyalGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { showForgotPassword = true }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Sign In / Sign Up Button
                Button(
                    onClick = {
                        if (isSignUp) {
                            viewModel.signUp(email, password)
                        } else {
                            viewModel.loginWithEmail(email, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RoyalGold,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isSignUp) "CREATE ACCOUNT" else "ACCESS ATILOLA AI",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Divider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(modifier = Modifier.weight(1f), color = RoyalGold.copy(alpha = 0.2f))
                    Text(
                        text = "OR CONTINUING WITH",
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.4f),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Divider(modifier = Modifier.weight(1f), color = RoyalGold.copy(alpha = 0.2f))
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Google sign in button
                OutlinedButton(
                    onClick = { viewModel.loginWithGoogle() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("google_button"),
                    border = BorderStroke(1.dp, RoyalGold.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.SmartToy, // Representing AI Sign In
                            contentDescription = "Google Icon representation",
                            tint = RoyalGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "INSTANT GOOGLE LOGIN",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isSignUp) "Already have an account? " else "Encountering Atilola first time? ",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 13.sp
                    )
                    Text(
                        text = if (isSignUp) "Login" else "Join",
                        color = RoyalGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.clickable { isSignUp = !isSignUp }
                    )
                }
            }
        }
    }

    // Forgot Password Dialog
    if (showForgotPassword) {
        AlertDialog(
            onDismissRequest = { showForgotPassword = false },
            title = {
                Text(
                    "Password Retrieval",
                    color = RoyalGold,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        "Input your email address below, and we will send you secure directions to restore access to your library.",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address", color = SoftGold) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoyalGold,
                            unfocusedBorderColor = RoyalGold.copy(alpha = 0.4f),
                            cursorColor = RoyalGold
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.forgotPassword(email) {
                            feedbackMessage = "A recovery link has been safely transmitted to $email."
                            showFeedbackDialog = true
                        }
                        showForgotPassword = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalGold, contentColor = Color.Black)
                ) {
                    Text("SEND CODE")
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgotPassword = false }) {
                    Text("CANCEL", color = RoyalGold)
                }
            },
            containerColor = CharcoalDark,
            tonalElevation = 6.dp
        )
    }

    // Feedback Dialog
    if (showFeedbackDialog) {
        AlertDialog(
            onDismissRequest = { showFeedbackDialog = false },
            title = { Text("Authentication", color = RoyalGold, fontFamily = FontFamily.Serif) },
            text = { Text(feedbackMessage, color = Color.White) },
            confirmButton = {
                Button(
                    onClick = { showFeedbackDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalGold)
                ) {
                    Text("OK", color = Color.Black)
                }
            },
            containerColor = CharcoalDark
        )
    }
}
