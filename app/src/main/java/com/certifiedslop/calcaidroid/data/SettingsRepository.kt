package com.certifiedslop.calcaidroid.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

enum class ApiProvider {
    OPENAI, ANTHROPIC, GEMINI, OLLAMA, OPENROUTER, CUSTOM
}

class SettingsRepository(private val context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val sharedPrefs = EncryptedSharedPreferences.create(
        "secure_settings",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val PROVIDER = stringPreferencesKey("api_provider")
    private val MODEL = stringPreferencesKey("model_string")
    private val SYSTEM_PROMPT = stringPreferencesKey("system_prompt")
    private val OLLAMA_ENDPOINT = stringPreferencesKey("ollama_endpoint")

    val apiProvider: Flow<ApiProvider> = context.dataStore.data.map { prefs ->
        try {
            ApiProvider.valueOf(prefs[PROVIDER] ?: ApiProvider.OPENAI.name)
        } catch (e: Exception) {
            ApiProvider.OPENAI
        }
    }

    val modelString: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[MODEL] ?: "gpt-4o"
    }

    val systemPrompt: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[SYSTEM_PROMPT] ?: "You are a calculator. Evaluate the mathematical expression provided by the user. Output ONLY the final numerical result. Do not output any explanations, text, or markdown formatting."
    }

    val ollamaEndpoint: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[OLLAMA_ENDPOINT] ?: "http://10.0.2.2:11434"
    }

    suspend fun updateProvider(provider: ApiProvider) {
        context.dataStore.edit { it[PROVIDER] = provider.name }
    }

    suspend fun updateModel(model: String) {
        context.dataStore.edit { it[MODEL] = model }
    }

    suspend fun updateSystemPrompt(prompt: String) {
        context.dataStore.edit { it[SYSTEM_PROMPT] = prompt }
    }

    suspend fun updateOllamaEndpoint(endpoint: String) {
        context.dataStore.edit { it[OLLAMA_ENDPOINT] = endpoint }
    }

    fun getApiKey(): String = sharedPrefs.getString("api_key", "") ?: ""

    fun saveApiKey(apiKey: String) {
        sharedPrefs.edit().putString("api_key", apiKey).apply()
    }
}
