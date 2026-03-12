package com.certifiedslop.calcaidroid.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certifiedslop.calcaidroid.data.ApiProvider
import com.certifiedslop.calcaidroid.data.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {

    val provider = repository.apiProvider.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ApiProvider.OPENAI)
    val modelString = repository.modelString.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "gpt-4o")
    val systemPrompt = repository.systemPrompt.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "You are a calculator. Evaluate the mathematical expression provided by the user. Output ONLY the final numerical result. Do not output any explanations, text, or markdown formatting.")
    val ollamaEndpoint = repository.ollamaEndpoint.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "http://10.0.2.2:11434")

    var apiKey by mutableStateOf(repository.getApiKey())
        private set

    fun updateProvider(newProvider: ApiProvider) {
        viewModelScope.launch {
            repository.updateProvider(newProvider)
        }
    }

    fun updateModel(newModel: String) {
        viewModelScope.launch {
            repository.updateModel(newModel)
        }
    }

    fun updateSystemPrompt(newPrompt: String) {
        viewModelScope.launch {
            repository.updateSystemPrompt(newPrompt)
        }
    }

    fun updateOllamaEndpoint(newEndpoint: String) {
        viewModelScope.launch {
            repository.updateOllamaEndpoint(newEndpoint)
        }
    }

    fun updateApiKey(newKey: String) {
        apiKey = newKey
        repository.saveApiKey(newKey)
    }
}
