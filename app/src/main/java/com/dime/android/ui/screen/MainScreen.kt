package com.dime.android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dime.android.viewmodel.TransactionViewModel
import com.dime.android.viewmodel.CategoryViewModel
import com.dime.android.viewmodel.BudgetViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Log : Screen("log", "Log", Icons.Default.List)
    object Insights : Screen("insights", "Insights", Icons.Default.Analytics)
    object Budget : Screen("budget", "Budget", Icons.Default.AccountBalance)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val transactionViewModel: TransactionViewModel = hiltViewModel()
    val categoryViewModel: CategoryViewModel = hiltViewModel()
    val budgetViewModel: BudgetViewModel = hiltViewModel()

    val screens = listOf(
        Screen.Log,
        Screen.Insights,
        Screen.Budget,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Log.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Log.route) {
                LogScreen(
                    transactionViewModel = transactionViewModel,
                    categoryViewModel = categoryViewModel
                )
            }
            composable(Screen.Insights.route) {
                InsightsScreen(
                    transactionViewModel = transactionViewModel,
                    categoryViewModel = categoryViewModel
                )
            }
            composable(Screen.Budget.route) {
                BudgetScreen(
                    budgetViewModel = budgetViewModel,
                    categoryViewModel = categoryViewModel
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
