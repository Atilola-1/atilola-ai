package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.api.WordExplanation
import com.example.data.model.Document
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.SoftGold
import com.example.ui.theme.CardCharcoal
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.WordExplanationState

@Composable
fun DocumentViewerScreen(
    viewModel: MainViewModel,
    onClose: () -> Unit
) {
    val document by viewModel.currentDocument.collectAsStateWithLifecycle()
    val explanationState by viewModel.wordExplanation.collectAsStateWithLifecycle()
    val activeWord by viewModel.activeWord.collectAsStateWithLifecycle()
    val isTtsPlaying by viewModel.isTtsPlaying.collectAsStateWithLifecycle()

    val fontSizeValue by viewModel.fontSize.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkMode.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    // Save reading position when scroll state changes
    LaunchedEffect(scrollState.value, document) {
        document?.let { doc ->
            val totalLength = doc.content.length
            if (totalLength > 0) {
                val ratio = scrollState.value.toFloat() / scrollState.maxValue.coerceAtLeast(1).toFloat()
                val activeCharIndex = (ratio * totalLength).toInt()
                viewModel.updateReadingPosition(doc.id, activeCharIndex, totalLength)
            }
        }
    }

    // Scroll back to last saved reading position on launch
    LaunchedEffect(document) {
        document?.let { doc ->
            if (doc.lastReadPosition > 0 && doc.content.isNotEmpty()) {
                val ratio = doc.lastReadPosition.toFloat() / doc.content.length.toFloat()
                val scrollTarget = (ratio * scrollState.maxValue).toInt()
                scrollState.scrollTo(scrollTarget)
            }
        }
    }

    if (document == null) {
        onClose()
        return
    }

    val doc = document!!

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = RoyalGold)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = doc.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        modifier = Modifier.widthIn(max = 160.dp)
                    )
                }

                // Controls: Font Zoom & Theme Selector
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.fontSize.value = (fontSizeValue - 2f).coerceAtLeast(12f) }) {
                        Icon(Icons.Default.Remove, contentDescription = "Zoom Out", tint = MaterialTheme.colorScheme.onSurface)
                    }
                    Text(
                        text = "${fontSizeValue.toInt()}pt",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalGold
                    )
                    IconButton(onClick = { viewModel.fontSize.value = (fontSizeValue + 2f).coerceAtMost(28f) }) {
                        Icon(Icons.Default.Add, contentDescription = "Zoom In", tint = MaterialTheme.colorScheme.onSurface)
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(onClick = { viewModel.isDarkMode.value = !isDarkTheme }) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Theme",
                            tint = RoyalGold
                        )
                    }
                }
            }
        },
        bottomBar = {
            // Text to speech & reading assistant panel
            Surface(
                tonalElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Audiotrack,
                            contentDescription = null,
                            tint = RoyalGold
                        )
                        Column {
                            Text("AI Voice Reader", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "Reading Language: ${doc.language}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Original voice reading
                        Button(
                            onClick = {
                                if (isTtsPlaying) {
                                    viewModel.stopTts()
                                } else {
                                    viewModel.startTts(doc.content, doc.language)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isTtsPlaying) Color.Red else RoyalGold,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (isTtsPlaying) Icons.Default.Stop else Icons.Default.VolumeUp,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isTtsPlaying) "STOP" else "READ ALOUD", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        // Translate audio reading
                        if (doc.language != "English") {
                            OutlinedButton(
                                onClick = {
                                    if (isTtsPlaying) {
                                        viewModel.stopTts()
                                    } else {
                                        viewModel.startTts(doc.content, "English")
                                    }
                                },
                                border = BorderStroke(1.dp, RoyalGold),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = RoyalGold)
                            ) {
                                Icon(Icons.Default.Translate, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("READ IN ENG", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Document Content styled as a premium reader viewport card
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(24.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                // Formatting metadata
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Format: ${doc.fileType}",
                        fontSize = 11.sp,
                        color = RoyalGold,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Reading Position: ${(doc.readingProgress * 100).toInt()}%",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Clicking helper text
                Text(
                    text = "💡 Tap on any word below to instantly translate or trigger AI Context explanations.",
                    fontSize = 12.sp,
                    color = RoyalGold.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(RoyalGold.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Build annotated string with highlight of active tapped word
                val annotatedString = buildAnnotatedString {
                    val text = doc.content
                    if (activeWord != null) {
                        val wordIndex = text.indexOf(activeWord!!, ignoreCase = true)
                        if (wordIndex != -1) {
                            append(text.substring(0, wordIndex))
                            withStyle(style = SpanStyle(background = RoyalGold.copy(alpha = 0.4f), color = RoyalGold, fontWeight = FontWeight.Bold)) {
                                append(text.substring(wordIndex, wordIndex + activeWord!!.length))
                            }
                            append(text.substring(wordIndex + activeWord!!.length))
                        } else {
                            append(text)
                        }
                    } else {
                        append(text)
                    }
                }

                ClickableText(
                    text = annotatedString,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = fontSizeValue.sp,
                        fontFamily = if (doc.language == "Arabic") FontFamily.Default else FontFamily.Serif,
                        lineHeight = (fontSizeValue * 1.6f).sp,
                        textAlign = if (doc.language == "Arabic") TextAlign.End else TextAlign.Start
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("document_text_view")
                        .padding(bottom = 120.dp),
                    onClick = { offset ->
                        val text = doc.content
                        if (text.isNotEmpty()) {
                            val word = findWordAtOffset(text, offset)
                            if (word.isNotBlank()) {
                                viewModel.selectWordAndExplain(word, text, offset)
                            }
                        }
                    }
                )
            }

            // Word Explanation Floating Glass Popup Overlay
            AnimatedVisibility(
                visible = activeWord != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, RoyalGold.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .testTag("word_popup"),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = activeWord ?: "",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = RoyalGold,
                                        fontFamily = FontFamily.Serif
                                    )
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Speak Word",
                                        tint = SoftGold,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable { viewModel.startTts(activeWord ?: "") }
                                    )
                                }
                                Text(
                                    text = "TAP WORD FOR AI CONTEXTUAL TRANSLATION",
                                    fontSize = 8.sp,
                                    color = SoftGold.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            IconButton(
                                onClick = { viewModel.dismissExplanation() },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                            }
                        }

                        Divider(color = RoyalGold.copy(alpha = 0.2f))

                        // Loader or Success
                        when (val state = explanationState) {
                            is WordExplanationState.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        CircularProgressIndicator(color = RoyalGold, modifier = Modifier.size(28.dp))
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            "AI Context Engine Analysing...",
                                            fontSize = 11.sp,
                                            color = RoyalGold,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            is WordExplanationState.Success -> {
                                val exp = state.explanation
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = exp.partOfSpeech,
                                            fontSize = 11.sp,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .background(SoftGold, RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )

                                        Text(
                                            text = exp.pronunciation,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    // Meanings
                                    Column {
                                        Text("Meaning", fontSize = 11.sp, color = RoyalGold, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = exp.meaning,
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    // AI Context Explanation
                                    Column {
                                        Text("AI Context Explanation", fontSize = 11.sp, color = RoyalGold, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = exp.contextExplanation,
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            style = TextStyle(fontWeight = FontWeight.Medium)
                                        )
                                    }

                                    // Synonyms
                                    if (exp.synonyms.isNotEmpty()) {
                                        Text(
                                            text = "Synonyms: ${exp.synonyms}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Action buttons: Save word
                                    Button(
                                        onClick = {
                                            viewModel.saveWord(exp, doc.language, doc.content)
                                            viewModel.dismissExplanation()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = RoyalGold, contentColor = Color.Black),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("SAVE TO VOCABULARY NOTEBOOK", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            is WordExplanationState.Error -> {
                                Text(
                                    text = state.message,
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

// Helper to determine the word tapped under a specific offset index
fun findWordAtOffset(text: String, offset: Int): String {
    if (text.isEmpty() || offset < 0 || offset >= text.length) return ""
    var start = offset
    // Walk backward while character is valid part of a word
    while (start > 0 && isWordChar(text[start - 1])) {
        start--
    }
    var end = offset
    // Walk forward while character is valid part of a word
    while (end < text.length && isWordChar(text[end])) {
        end++
    }
    if (start >= end) return ""
    return text.substring(start, end)
}

fun isWordChar(char: Char): Boolean {
    return char.isLetterOrDigit() || char == '\'' || char == '-' || char == 'ُ' || char == 'َ' || char == 'ِ' || char == 'ّ' || char == 'ْ' || char == 'ً' || char == 'ٌ' || char == 'ٍ' // support Arabic diacritics
}
