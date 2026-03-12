package com.certifiedslop.calcaidroid.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certifiedslop.calcaidroid.data.CalculatorRepository
import kotlinx.coroutines.launch

class CalculatorViewModel(private val repository: CalculatorRepository) : ViewModel() {

    var displayText by mutableStateOf("")
        private set

    var resultText by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onInput(input: String) {
        displayText += input
    }

    fun onClear() {
        displayText = ""
        resultText = ""
    }

    fun onDelete() {
        if (displayText.isNotEmpty()) {
            displayText = displayText.dropLast(1)
        }
    }

    fun onCalculate() {
        if (displayText.isBlank()) return

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.calculate(displayText)
            result.onSuccess {
                resultText = it
            }.onFailure {
                errorMessage = it.message ?: "Unknown error"
            }
            isLoading = false
        }
    }

    fun onAiPrompt(prompt: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.calculate(prompt)
            result.onSuccess {
                displayText = prompt
                resultText = it
            }.onFailure {
                errorMessage = it.message ?: "Unknown error"
            }
            isLoading = false
        }
    }

    fun clearError() {
        errorMessage = null
    }
}
