package com.certifiedslop.calcaidroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.certifiedslop.calcaidroid.data.CalculatorRepository
import com.certifiedslop.calcaidroid.data.SettingsRepository
import com.certifiedslop.calcaidroid.ui.*
import com.certifiedslop.calcaidroid.ui.theme.CalcAIdroidTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val settingsRepository = SettingsRepository(applicationContext)
        val calculatorRepository = CalculatorRepository(settingsRepository)

        setContent {
            CalcAIdroidTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "calculator") {
                    composable("calculator") {
                        val viewModel: CalculatorViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return CalculatorViewModel(calculatorRepository) as T
                                }
                            }
                        )
                        CalculatorScreen(
                            viewModel = viewModel,
                            onNavigateToSettings = { navController.navigate("settings") }
                        )
                    }
                    composable("settings") {
                        val viewModel: SettingsViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return SettingsViewModel(settingsRepository) as T
                                }
                            }
                        )
                        SettingsScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
