package com.example.androidmodularshowcase.ui.feed

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun FeedScreen(innerPadding: PaddingValues) {
    val items = remember {
        (1..10).map { i ->
            "第 $i 条：这是占位的列表数据，用来测试 LazyColumn。"
        }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = innerPadding,
    ) {
        items(items) { title ->
            Card {
                ListItem(
                    headlineContent = { Text(title, style = MaterialTheme.typography.titleMedium) },
                    supportingContent = { Text("副标题：描述/时间/作者等占位信息") },
                )
            }
        }
    }
}
