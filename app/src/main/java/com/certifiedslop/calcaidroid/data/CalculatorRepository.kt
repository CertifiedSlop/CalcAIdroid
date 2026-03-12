package com.certifiedslop.calcaidroid.data

import com.certifiedslop.calcaidroid.data.api.*
import kotlinx.coroutines.flow.first
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CalculatorRepository(private val settingsRepository: SettingsRepository) {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.example.com/") // Dummy base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(AIApiService::class.java)

    suspend fun calculate(expression: String): Result<String> {
        val provider = settingsRepository.apiProvider.first()
        val model = settingsRepository.modelString.first()
        val systemPrompt = settingsRepository.systemPrompt.first()
        val apiKey = settingsRepository.getApiKey()
        val ollamaBaseUrl = settingsRepository.ollamaEndpoint.first()

        return try {
            val result = when (provider) {
                ApiProvider.OPENAI -> {
                    val response = apiService.calculateOpenAi(
                        url = "https://api.openai.com/v1/chat/completions",
                        auth = "Bearer $apiKey",
                        request = OpenAiRequest(
                            model = model,
                            messages = listOf(
                                Message("system", systemPrompt),
                                Message("user", expression)
                            )
                        )
                    )
                    response.choices.firstOrNull()?.message?.content ?: "No result"
                }
                ApiProvider.ANTHROPIC -> {
                    val response = apiService.calculateAnthropic(
                        url = "https://api.anthropic.com/v1/messages",
                        apiKey = apiKey,
                        request = AnthropicRequest(
                            model = model,
                            system = systemPrompt,
                            messages = listOf(Message("user", expression))
                        )
                    )
                    response.content.firstOrNull()?.text ?: "No result"
                }
                ApiProvider.GEMINI -> {
                    val response = apiService.calculateGemini(
                        url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey",
                        request = GeminiRequest(
                            systemInstruction = GeminiSystemInstruction(listOf(GeminiPart(systemPrompt))),
                            contents = listOf(GeminiContent(listOf(GeminiPart(expression))))
                        )
                    )
                    response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "No result"
                }
                ApiProvider.OLLAMA -> {
                    // Ensure URL ends correctly for the chat API
                    val url = if (ollamaBaseUrl.endsWith("/")) "${ollamaBaseUrl}api/chat" else "$ollamaBaseUrl/api/chat"
                    val response = apiService.calculateOllama(
                        url = url,
                        request = OllamaRequest(
                            model = model,
                            messages = listOf(
                                Message("system", systemPrompt),
                                Message("user", expression)
                            )
                        )
                    )
                    response.message.content
                }
                ApiProvider.OPENROUTER -> {
                    val response = apiService.calculateOpenRouter(
                        url = "https://openrouter.ai/api/v1/chat/completions",
                        auth = "Bearer $apiKey",
                        request = OpenAiRequest(
                            model = model,
                            messages = listOf(
                                Message("system", systemPrompt),
                                Message("user", expression)
                            )
                        )
                    )
                    response.choices.firstOrNull()?.message?.content ?: "No result"
                }
                ApiProvider.CUSTOM -> {
                    val response = apiService.calculateOpenAi(
                        url = model,
                        auth = "Bearer $apiKey",
                        request = OpenAiRequest(
                            model = "default",
                            messages = listOf(
                                Message("system", systemPrompt),
                                Message("user", expression)
                            )
                        )
                    )
                    response.choices.firstOrNull()?.message?.content ?: "No result"
                }
            }
            Result.success(result.trim())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
