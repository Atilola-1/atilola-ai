package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RoyalGold
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by viewModel.isDarkMode.collectAsStateWithLifecycle()
            
            MyApplicationTheme(darkTheme = isDarkTheme) {
                val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
                val currentDocument by viewModel.currentDocument.collectAsStateWithLifecycle()
                
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (currentUser == null) {
                        LoginScreen(viewModel)
                    } else {
                        // User logged in shell
                        var currentTab by remember { mutableIntStateOf(0) }
                        
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            bottomBar = {
                                if (currentDocument == null) {
                                    NavigationBar(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        tonalElevation = 8.dp
                                    ) {
                                        NavigationBarItem(
                                            icon = { Icon(Icons.Default.Book, contentDescription = "Library") },
                                            label = { Text("Library") },
                                            selected = currentTab == 0,
                                            onClick = { currentTab = 0 },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = Color.Black,
                                                selectedTextColor = RoyalGold,
                                                indicatorColor = RoyalGold,
                                                unselectedIconColor = Color.Gray,
                                                unselectedTextColor = Color.Gray
                                            )
                                        )
                                        NavigationBarItem(
                                            icon = { Icon(Icons.Default.CollectionsBookmark, contentDescription = "Notebook") },
                                            label = { Text("Notebook") },
                                            selected = currentTab == 1,
                                            onClick = { currentTab = 1 },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = Color.Black,
                                                selectedTextColor = RoyalGold,
                                                indicatorColor = RoyalGold,
                                                unselectedIconColor = Color.Gray,
                                                unselectedTextColor = Color.Gray
                                            )
                                        )
                                        NavigationBarItem(
                                            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                                            label = { Text("Settings") },
                                            selected = currentTab == 2,
                                            onClick = { currentTab = 2 },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = Color.Black,
                                                selectedTextColor = RoyalGold,
                                                indicatorColor = RoyalGold,
                                                unselectedIconColor = Color.Gray,
                                                unselectedTextColor = Color.Gray
                                            )
                                        )
                                    }
                                }
                            }
                        ) { innerPadding ->
                            Box(modifier = Modifier.padding(innerPadding)) {
                                when (currentTab) {
                                    0 -> HomeScreen(
                                        viewModel = viewModel,
                                        onNavigateToDocument = { /* Handled declaratively by VM state */ }
                                    )
                                    1 -> VocabularyNotebookScreen(viewModel = viewModel)
                                    2 -> SettingsScreen(viewModel = viewModel)
                                }
                                
                                // Beautiful transition overlay for Document Reader
                                AnimatedVisibility(
                                    visible = currentDocument != null,
                                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                                ) {
                                    DocumentViewerScreen(
                                        viewModel = viewModel,
                                        onClose = { viewModel.closeDocument() }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
