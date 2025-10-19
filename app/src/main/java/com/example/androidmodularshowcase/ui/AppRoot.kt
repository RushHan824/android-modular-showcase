package com.example.androidmodularshowcase.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.androidmodularshowcase.ui.feed.FeedScreen
import com.example.androidmodularshowcase.ui.profile.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot() {
    var tab by remember { mutableStateOf(0) }
    val tabs = listOf("Feed", "Profile")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Android Modular Showcase") }) },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { i, label ->
                    NavigationBarItem(
                        selected = tab == i,
                        onClick = { tab = i },
                        icon = {},
                        label = { Text(label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        when (tab) {
            0 -> FeedScreen(innerPadding)
            else -> ProfileScreen(innerPadding)
        }
    }
}

@Composable
fun Center(text: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text, style = MaterialTheme.typography.headlineMedium)
    }
}
