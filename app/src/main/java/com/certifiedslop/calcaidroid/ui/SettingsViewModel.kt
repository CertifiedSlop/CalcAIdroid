package com.certifiedslop.calcaidroid.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certifiedslop.calcaidroid.data.ApiProvider
import com.certifiedslop.calcaidroid.data.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {

    val provider = repository.apiProvider.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ApiProvider.OPENAI)

    var modelString by mutableStateOf("gpt-4o")
        private set

    var systemPrompt by mutableStateOf("You are a calculator. Evaluate the mathematical expression provided by the user. Output ONLY the final numerical result. Do not output any explanations, text, or markdown formatting.")
        private set

    var ollamaEndpoint by mutableStateOf("http://10.0.2.2:11434")
        private set

    var apiKey by mutableStateOf(repository.getApiKey())
        private set

    init {
        viewModelScope.launch {
            modelString = repository.modelString.first()
            systemPrompt = repository.systemPrompt.first()
            ollamaEndpoint = repository.ollamaEndpoint.first()
        }
    }

    fun updateProvider(newProvider: ApiProvider) {
        viewModelScope.launch {
            repository.updateProvider(newProvider)
        }
    }

    fun updateModel(newModel: String) {
        modelString = newModel
        viewModelScope.launch {
            repository.updateModel(newModel)
        }
    }

    fun updateSystemPrompt(newPrompt: String) {
        systemPrompt = newPrompt
        viewModelScope.launch {
            repository.updateSystemPrompt(newPrompt)
        }
    }

    fun updateOllamaEndpoint(newEndpoint: String) {
        ollamaEndpoint = newEndpoint
        viewModelScope.launch {
            repository.updateOllamaEndpoint(newEndpoint)
        }
    }

    fun updateApiKey(newKey: String) {
        apiKey = newKey
        repository.saveApiKey(newKey)
    }
}
