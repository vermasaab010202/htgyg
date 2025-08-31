package com.dime.android.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dime.android.data.entity.Category
import com.dime.android.data.entity.Transaction
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    categories: List<Category>,
    transaction: Transaction? = null,
    onDismiss: () -> Unit,
    onSave: (Transaction) -> Unit
) {
    var amount by remember { mutableStateOf(transaction?.amount?.toString() ?: "") }
    var note by remember { mutableStateOf(transaction?.note ?: "") }
    var selectedCategory by remember { mutableStateOf<Category?>(
        transaction?.let { t -> categories.find { it.id == t.categoryId } }
    ) }
    var isIncome by remember { mutableStateOf(transaction?.income ?: false) }
    var selectedDate by remember { mutableStateOf(transaction?.date ?: Date()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var recurringType by remember { mutableStateOf(transaction?.recurringType ?: 0) }
    var recurringCoefficient by remember { mutableStateOf(transaction?.recurringCoefficient ?: 1) }

    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val filteredCategories = categories.filter { it.income == isIncome }

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
                    text = if (transaction == null) "Add Transaction" else "Edit Transaction",
                    style = MaterialTheme.typography.headlineSmall
                )

                // Income/Expense Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        onClick = { isIncome = false },
                        label = { Text("Expense") },
                        selected = !isIncome,
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        onClick = { isIncome = true },
                        label = { Text("Income") },
                        selected = isIncome,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Amount
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
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
                        filteredCategories.forEach { category ->
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

                // Note
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Date
                OutlinedTextField(
                    value = dateFormat.format(selectedDate),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Date") },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Recurring options
                if (recurringType > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Repeat every:")
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = recurringCoefficient.toString(),
                            onValueChange = { 
                                it.toIntOrNull()?.let { value -> 
                                    recurringCoefficient = value 
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.width(80.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            when (recurringType) {
                                1 -> if (recurringCoefficient == 1) "day" else "days"
                                2 -> if (recurringCoefficient == 1) "week" else "weeks"
                                3 -> if (recurringCoefficient == 1) "month" else "months"
                                else -> ""
                            }
                        )
                    }
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
                                val newTransaction = Transaction(
                                    id = transaction?.id ?: UUID.randomUUID().toString(),
                                    amount = amountValue,
                                    note = note,
                                    date = selectedDate,
                                    day = selectedDate,
                                    month = selectedDate,
                                    categoryId = selectedCategory!!.id,
                                    income = isIncome,
                                    recurringType = recurringType,
                                    recurringCoefficient = recurringCoefficient
                                )
                                onSave(newTransaction)
                            }
                        }
                    ) {
                        Text(if (transaction == null) "Add" else "Update")
                    }
                }
            }
        }
    }

    // Date Picker Dialog would go here
    // For simplicity, using a basic implementation
}
