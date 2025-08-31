package com.dime.android.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dime.android.data.entity.Budget
import com.dime.android.data.entity.Category
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBudgetDialog(
    categories: List<Category>,
    budget: Budget? = null,
    onDismiss: () -> Unit,
    onSave: (Budget) -> Unit
) {
    var amount by remember { mutableStateOf(budget?.amount?.toString() ?: "") }
    var selectedCategory by remember { mutableStateOf<Category?>(
        budget?.let { b -> categories.find { it.id == b.categoryId } }
    ) }
    var budgetType by remember { mutableStateOf(budget?.type ?: 0) }
    var isGreen by remember { mutableStateOf(budget?.green ?: false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (budget == null) "Add Budget" else "Edit Budget",
                    style = MaterialTheme.typography.headlineSmall
                )

                // Amount
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Budget Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                // Category Dropdown
                var expandedCategory by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = { expandedCategory = !expandedCategory }
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.let { "${it.emoji} ${it.name}" } ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text("${category.emoji} ${category.name}") },
                                onClick = {
                                    selectedCategory = category
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }

                // Budget Type
                Text("Budget Period", style = MaterialTheme.typography.bodyMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        onClick = { budgetType = 2 },
                        label = { Text("Daily") },
                        selected = budgetType == 2,
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        onClick = { budgetType = 1 },
                        label = { Text("Weekly") },
                        selected = budgetType == 1,
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        onClick = { budgetType = 0 },
                        label = { Text("Monthly") },
                        selected = budgetType == 0,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Green budget toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sustainable Budget")
                    Switch(
                        checked = isGreen,
                        onCheckedChange = { isGreen = it }
                    )
                }

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val amountValue = amount.toDoubleOrNull() ?: 0.0
                            if (amountValue > 0 && selectedCategory != null) {
                                val newBudget = Budget(
                                    id = budget?.id ?: UUID.randomUUID().toString(),
                                    amount = amountValue,
                                    categoryId = selectedCategory!!.id,
                                    type = budgetType,
                                    green = isGreen,
                                    startDate = Date(),
                                    dateCreated = budget?.dateCreated ?: Date()
                                )
                                onSave(newBudget)
                            }
                        }
                    ) {
                        Text(if (budget == null) "Add" else "Update")
                    }
                }
            }
        }
    }
}
