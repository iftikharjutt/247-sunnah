package com.sunnah.app.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private var chat = aiProvider?.getModel()?.startChat()

    fun init(apiKey: String) {
        if (aiProvider == null) {
            aiProvider = AiProvider(apiKey)
            chat = aiProvider?.getModel()?.startChat()
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        
        _messages.add(ChatMessage(text, true))
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = chat?.sendMessage(text)
                response?.text?.let {
                    _messages.add(ChatMessage(it, false))
                }
            } catch (e: Exception) {
                _messages.add(ChatMessage("Error: ${e.message}", false))
            } finally {
                _isLoading.value = false
            }
        }
    }
}
