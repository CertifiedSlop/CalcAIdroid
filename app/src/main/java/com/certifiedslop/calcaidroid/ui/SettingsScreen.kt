package com.certifiedslop.calcaidroid.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.certifiedslop.calcaidroid.data.ApiProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val provider by viewModel.provider.collectAsState()
    val modelString by viewModel.modelString.collectAsState()
    val systemPrompt by viewModel.systemPrompt.collectAsState()
    val ollamaEndpoint by viewModel.ollamaEndpoint.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // API Provider Dropdown
            Text("API Provider", style = MaterialTheme.typography.titleMedium)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = provider.name,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    ApiProvider.entries.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption.name) },
                            onClick = {
                                viewModel.updateProvider(selectionOption)
                                expanded = false
                            }
                        )
                    }
                }
            }

            // API Key Field (Optional for Ollama)
            if (provider != ApiProvider.OLLAMA) {
                Text("API Key", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = viewModel.apiKey,
                    onValueChange = { viewModel.updateApiKey(it) },
                    label = { Text("Enter API Key") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
            }

            // Ollama Endpoint Field
            if (provider == ApiProvider.OLLAMA) {
                Text("Ollama Endpoint", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = ollamaEndpoint,
                    onValueChange = { viewModel.updateOllamaEndpoint(it) },
                    label = { Text("e.g. http://10.0.2.2:11434") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Model Selection
            val modelLabel = when (provider) {
                ApiProvider.OLLAMA -> "Model Name (e.g. llama3)"
                ApiProvider.CUSTOM -> "Full Endpoint URL"
                else -> "Model ID (e.g. gpt-4o, claude-3-opus)"
            }
            Text("Model / Endpoint", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = modelString,
                onValueChange = { viewModel.updateModel(it) },
                label = { Text(modelLabel) },
                modifier = Modifier.fillMaxWidth()
            )

            // System Prompt
            Text("System Prompt", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = systemPrompt,
                onValueChange = { viewModel.updateSystemPrompt(it) },
                label = { Text("How the AI should behave") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
        }
    }
}
