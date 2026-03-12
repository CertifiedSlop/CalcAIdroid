package com.certifiedslop.calcaidroid.data.api

import com.google.gson.annotations.SerializedName

data class OpenAiRequest(
    val model: String,
    val messages: List<Message>,
    val stream: Boolean = false
)

data class Message(
    val role: String,
    val content: String
)

data class OpenAiResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)

data class AnthropicRequest(
    val model: String,
    @SerializedName("max_tokens") val maxTokens: Int = 1024,
    val system: String,
    val messages: List<Message>
)

data class AnthropicResponse(
    val content: List<Content>
)

data class Content(
    val text: String
)

data class GeminiRequest(
    val contents: List<GeminiContent>,
    @SerializedName("system_instruction") val systemInstruction: GeminiSystemInstruction? = null
)

data class GeminiSystemInstruction(
    val parts: List<GeminiPart>
)

data class GeminiContent(
    val parts: List<GeminiPart>
)

data class GeminiPart(
    val text: String
)

data class GeminiResponse(
    val candidates: List<GeminiCandidate>
)

data class GeminiCandidate(
    val content: GeminiContent
)

data class OllamaRequest(
    val model: String,
    val messages: List<Message>,
    val stream: Boolean = false
)

data class OllamaResponse(
    val message: Message
)
