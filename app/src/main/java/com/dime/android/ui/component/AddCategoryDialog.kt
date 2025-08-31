package com.dime.android.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dime.android.data.entity.Category
import com.dime.android.model.SuggestedCategory
import java.util.*

@Composable
fun AddCategoryDialog(
    category: Category? = null,
    isIncome: Boolean,
    onDismiss: () -> Unit,
    onSave: (Category) -> Unit
) {
    var name by remember { mutableStateOf(category?.name ?: "") }
    var selectedEmoji by remember { mutableStateOf(category?.emoji ?: "💰") }
    var selectedColor by remember { mutableStateOf(category?.colour ?: "#3B82F6") }

    val suggestedCategories = if (isIncome) SuggestedCategory.incomes else SuggestedCategory.expenses
    val colors = listOf(
        "#3B82F6", "#10B981", "#F59E0B", "#EF4444", "#8B5CF6",
        "#EC4899", "#06B6D4", "#84CC16", "#F97316", "#6B7280"
    )

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
                    text = if (category == null) "Add Category" else "Edit Category",
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Emoji")
                    Text(
                        text = selectedEmoji,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.clickable { 
                            selectedEmoji = if (selectedEmoji == "💰") "🍔" else "💰"
                        }
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(80.dp)
                ) {
                    items(colors) { colorHex ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color(android.graphics.Color.parseColor(colorHex)),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedColor = colorHex },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedColor == colorHex) {
                                Text("✓", color = Color.White)
                            }
                        }
                    }
                }

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
                            if (name.isNotBlank()) {
                                val newCategory = Category(
                                    id = category?.id ?: UUID.randomUUID().toString(),
                                    name = name,
                                    emoji = selectedEmoji,
                                    colour = selectedColor,
                                    income = isIncome,
                                    dateCreated = category?.dateCreated ?: Date(),
                                    order = category?.order ?: 0
                                )
                                onSave(newCategory)
                            }
                        }
                    ) {
                        Text(if (category == null) "Add" else "Update")
                    }
                }
            }
        }
    }
}
