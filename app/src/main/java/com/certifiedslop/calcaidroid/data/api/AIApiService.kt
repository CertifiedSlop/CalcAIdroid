package com.certifiedslop.calcaidroid.data.api

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface AIApiService {
    @POST
    suspend fun calculateOpenAi(
        @Url url: String,
        @Header("Authorization") auth: String,
        @Body request: OpenAiRequest
    ): OpenAiResponse

    @POST
    suspend fun calculateAnthropic(
        @Url url: String,
        @Header("x-api-key") apiKey: String,
        @Header("anthropic-version") version: String = "2023-06-01",
        @Body request: AnthropicRequest
    ): AnthropicResponse

    @POST
    suspend fun calculateGemini(
        @Url url: String,
        @Body request: GeminiRequest
    ): GeminiResponse

    @POST
    suspend fun calculateOllama(
        @Url url: String,
        @Body request: OllamaRequest
    ): OllamaResponse

    @POST
    suspend fun calculateOpenRouter(
        @Url url: String,
        @Header("Authorization") auth: String,
        @Header("HTTP-Referer") referer: String = "https://github.com/calcaidroid",
        @Header("X-Title") title: String = "CalcAIdroid",
        @Body request: OpenAiRequest
    ): OpenAiResponse
}
