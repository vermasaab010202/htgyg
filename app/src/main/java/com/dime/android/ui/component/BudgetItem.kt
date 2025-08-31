package com.dime.android.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dime.android.data.entity.Budget
import com.dime.android.data.entity.Category
import java.text.NumberFormat

@Composable
fun BudgetItem(
    budget: Budget,
    categories: List<Category>,
    onEdit: (Budget) -> Unit,
    onDelete: (Budget) -> Unit
) {
    val category = categories.find { it.id == budget.categoryId }
    val currencyFormat = NumberFormat.getCurrencyInstance()
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category indicator
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = try {
                                    Color(android.graphics.Color.parseColor(category?.colour ?: "#6B7280"))
                                } catch (e: Exception) {
                                    Color(0xFF6B7280)
                                },
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category?.emoji ?: "💰",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = category?.name ?: "Unknown Category",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = when (budget.type) {
                                0 -> "Monthly Budget"
                                1 -> "Weekly Budget"
                                2 -> "Daily Budget"
                                else -> "Budget"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                // Amount
                Text(
                    text = currencyFormat.format(budget.amount),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (budget.green) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }

            // Progress bar placeholder (would need actual spending data)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = 0.6f, // This would be calculated based on actual spending
                modifier = Modifier.fillMaxWidth()
            )
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onEdit(budget) }) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit")
                }
                TextButton(onClick = { onDelete(budget) }) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete")
                }
            }
        }
    }
}
