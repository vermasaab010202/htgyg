package com.dime.android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dime.android.ui.component.InsightCard
import com.dime.android.ui.component.SpendingChart
import com.dime.android.viewmodel.CategoryViewModel
import com.dime.android.viewmodel.TransactionViewModel
import java.text.NumberFormat
import java.util.*

@Composable
fun InsightsScreen(
    transactionViewModel: TransactionViewModel,
    categoryViewModel: CategoryViewModel
) {
    val transactions by transactionViewModel.transactions.collectAsState()
    val categories by categoryViewModel.categories.collectAsState()
    
    val currencyFormat = NumberFormat.getCurrencyInstance()
    
    // Calculate insights
    val totalIncome = transactions.filter { it.income }.sumOf { it.amount }
    val totalExpenses = transactions.filter { !it.income }.sumOf { it.amount }
    val netAmount = totalIncome - totalExpenses
    
    // Group expenses by category
    val expensesByCategory = transactions
        .filter { !it.income }
        .groupBy { it.categoryId }
        .mapValues { (_, transactions) -> transactions.sumOf { it.amount } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Insights",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (transactions.isEmpty()) {
            // Empty state
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "📈",
                    style = MaterialTheme.typography.displayLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No Data Available",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Add some transactions to see insights",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Summary Cards
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        InsightCard(
                            title = "Income",
                            amount = currencyFormat.format(totalIncome),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                        InsightCard(
                            title = "Expenses",
                            amount = currencyFormat.format(totalExpenses),
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    InsightCard(
                        title = "Net Amount",
                        amount = currencyFormat.format(netAmount),
                        color = if (netAmount >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Spending Chart
                if (expensesByCategory.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Spending by Category",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                SpendingChart(
                                    expensesByCategory = expensesByCategory,
                                    categories = categories
                                )
                            }
                        }
                    }
                }

                // Top Categories
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Top Spending Categories",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            expensesByCategory.entries
                                .sortedByDescending { it.value }
                                .take(5)
                                .forEach { (categoryId, amount) ->
                                    val category = categories.find { it.id == categoryId }
                                    if (category != null) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("${category.emoji} ${category.name}")
                                            Text(
                                                currencyFormat.format(amount),
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                        }
                    }
                }
            }
        }
    }
}
