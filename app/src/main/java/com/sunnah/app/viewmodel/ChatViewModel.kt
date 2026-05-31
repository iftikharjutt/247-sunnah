package com.sunnah.app.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.ChatSession
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.sunnah.app.data.AiProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatMessage(val text: String, val isUser: Boolean)

class ChatViewModel : ViewModel() {
    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var aiProvider: AiProvider? = null
    private var chat: ChatSession? = null

    fun init(apiKey: String) {
        if (aiProvider == null || aiProvider?.getModel()?.apiKey != apiKey) {
            aiProvider = AiProvider(apiKey)
            chat = aiProvider?.getModel()?.startChat()
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        
        if (chat == null) {
            _messages.add(ChatMessage("Error: API not initialized. Please check your key.", false))
            return
        }

        _messages.add(ChatMessage(text, true))
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = chat?.sendMessage(text)
                response?.text?.let {
                    _messages.add(ChatMessage(it, false))
                } ?: run {
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
