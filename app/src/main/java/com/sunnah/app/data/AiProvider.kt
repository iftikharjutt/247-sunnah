package com.sunnah.app.data

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

class AiProvider(apiKey: String) {
    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey,
        systemInstruction = content {
            text("You are '24/7 Sunnah', a highly knowledgeable companion for Muslims. Your task is to provide guidance on prophetic traditions (Sunnah) based strictly on canonical sources, primarily Sahih al-Bukhari and Sahih Muslim. Always provide authentic citations. If a practice is not found in canonical Sunnah, state so clearly. Be professional, encouraging, and clear.")
        }
    )

    fun getModel() = model
}
