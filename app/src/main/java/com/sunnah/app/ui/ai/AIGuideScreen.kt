package com.sunnah.app.ui.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sunnah.app.viewmodel.ChatViewModel
import com.sunnah.app.viewmodel.MainViewModel

@Composable
fun AIGuideScreen(mainViewModel: MainViewModel) {
    val chatViewModel: ChatViewModel = viewModel()
    val savedApiKey by mainViewModel.apiKey.collectAsState(initial = null)
    
    if (savedApiKey.isNullOrBlank()) {
        ApiSetupScreen { mainViewModel.saveApiKey(it) }
    } else {
        LaunchedEffect(savedApiKey) {
            chatViewModel.init(savedApiKey!!)
        }

        val messages = chatViewModel.messages
        val isLoading by chatViewModel.isLoading.collectAsState()
        var inputText by remember { mutableStateOf("") }
        val listState = rememberLazyListState()
        val focusManager = LocalFocusManager.current

        LaunchedEffect(messages.size) {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(messages.size - 1)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(messages) { message ->
                        ChatBubble(message)
                    }
                    if (isLoading) {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.CenterStart),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }

            QuickSuggestions {
                inputText = it
                chatViewModel.sendMessage(it)
                focusManager.clearFocus()
            }

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ask about Sunnah...") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        chatViewModel.sendMessage(inputText)
                        inputText = ""
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(24.dp))
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun ApiSetupScreen(onSave: (String) -> Unit) {
    var key by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("AI Companion Setup", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "To use the Prophetic Guide, please enter your Gemini API key from Google AI Studio.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = key,
            onValueChange = { key = it },
            label = { Text("Gemini API Key") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { if (key.isNotBlank()) onSave(key) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Save & Continue")
        }
    }
}

@Composable
fun ChatBubble(message: com.sunnah.app.viewmodel.ChatMessage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isUser) 16.dp else 0.dp,
                bottomEnd = if (message.isUser) 0.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser) MaterialTheme.colorScheme.primary else Color.White
            )
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = if (message.isUser) Color.White else MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
fun QuickSuggestions(onSelect: (String) -> Unit) {
    val suggestions = listOf("Siwak Sunnah", "Midday Nap", "Tahajjud Timings")
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        suggestions.forEach { suggestion ->
            SuggestionChip(
                onClick = { onSelect(suggestion) },
                label = { Text(suggestion) },
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}
