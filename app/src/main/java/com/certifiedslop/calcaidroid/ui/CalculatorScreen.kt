package com.certifiedslop.calcaidroid.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    onNavigateToSettings: () -> Unit
) {
    var showAiDialog by remember { mutableStateOf(false) }
    var aiPrompt by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("CalcAIdroid") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAiDialog = true }) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "AI Prompt")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Display Area
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = viewModel.displayText,
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 2,
                        textAlign = TextAlign.End
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = viewModel.resultText,
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Numpad
            val buttons = listOf(
                "C", "DEL", "/", "*",
                "7", "8", "9", "-",
                "4", "5", "6", "+",
                "1", "2", "3", "=",
                "0", "."
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(2f)
            ) {
                items(buttons) { button ->
                    val isOperator = button in listOf("/", "*", "-", "+", "=", "C", "DEL")
                    Button(
                        onClick = {
                            when (button) {
                                "C" -> viewModel.onClear()
                                "DEL" -> viewModel.onDelete()
                                "=" -> viewModel.onCalculate()
                                else -> viewModel.onInput(button)
                            }
                        },
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxSize(),
                        colors = if (button == "=") {
                            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        } else if (isOperator) {
                            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
                        } else {
                            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                        },
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Text(
                            text = button,
                            style = MaterialTheme.typography.titleLarge,
                            color = if (isOperator && button != "=") MaterialTheme.colorScheme.onSecondaryContainer else if (button == "=") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }

    if (showAiDialog) {
        AlertDialog(
            onDismissRequest = { showAiDialog = false },
            title = { Text("AI Calculation Prompt") },
            text = {
                OutlinedTextField(
                    value = aiPrompt,
                    onValueChange = { aiPrompt = it },
                    label = { Text("Enter natural language math problem") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onAiPrompt(aiPrompt)
                    showAiDialog = false
                    aiPrompt = ""
                }) {
                    Text("Ask AI")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAiDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
