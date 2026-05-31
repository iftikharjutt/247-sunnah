package com.sunnah.app.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunnah.app.data.AiProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content

data class ChatMessage(val text: String, val isUser: Boolean)

class ChatViewModel : ViewModel() {
    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var aiProvider: AiProvider? = null
    private val chatHistory = mutableListOf<Content>()

    fun init(apiKey: String) {
        if (aiProvider == null || aiProvider?.getModel()?.apiKey != apiKey) {
            aiProvider = AiProvider(apiKey)
            chatHistory.clear()
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        
        val model = aiProvider?.getModel()
        if (model == null) {
            _messages.add(ChatMessage("Error: AI not initialized. Please check your key.", false))
            return
        }

        _messages.add(ChatMessage(text, true))
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // Add user message to history
                val userContent = content("user") { text(text) }
                chatHistory.add(userContent)

                // Generate response using full history for context
                val response = model.generateContent(*chatHistory.toTypedArray())
                val responseText = response.text
                
                if (responseText != null) {
                    _messages.add(ChatMessage(responseText, false))
                    // Add model response to history
                    chatHistory.add(content("model") { text(responseText) })
                } else {
                    _messages.add(ChatMessage("Error: Received empty response from AI.", false))
                }
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Unknown error occurred"
                _messages.add(ChatMessage("API Error: $errorMsg", false))
                android.util.Log.e("ChatViewModel", "Message failed", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
