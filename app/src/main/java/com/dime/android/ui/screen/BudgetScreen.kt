package com.dime.android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dime.android.ui.component.BudgetItem
import com.dime.android.ui.component.AddBudgetDialog
import com.dime.android.viewmodel.BudgetViewModel
import com.dime.android.viewmodel.CategoryViewModel

@Composable
fun BudgetScreen(
    budgetViewModel: BudgetViewModel,
    categoryViewModel: CategoryViewModel
) {
    val budgets by budgetViewModel.budgets.collectAsState()
    val mainBudgets by budgetViewModel.mainBudgets.collectAsState()
    val categories by categoryViewModel.categories.collectAsState()
    val isLoading by budgetViewModel.isLoading.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Budgets",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            IconButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Budget")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content
        if (budgets.isEmpty() && mainBudgets.isEmpty()) {
            // Empty state
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "📊",
                    style = MaterialTheme.typography.displayLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Budget Your Finances",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Link budgets to categories and set appropriate expenditure goals",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // Budget list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(budgets) { budget ->
                    BudgetItem(
                        budget = budget,
                        categories = categories,
                        onEdit = { budgetViewModel.updateBudget(it) },
                        onDelete = { budgetViewModel.deleteBudget(it) }
                    )
                }
            }
        }
    }

    // Add Budget Dialog
    if (showAddDialog) {
        AddBudgetDialog(
            categories = categories.filter { !it.income },
            onDismiss = { showAddDialog = false },
            onSave = { budget ->
                budgetViewModel.addBudget(budget)
                showAddDialog = false
            }
        )
    }

    // Loading indicator
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
