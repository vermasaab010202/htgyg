package com.dime.android.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.dime.android.data.entity.Category
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SpendingChart(
    expensesByCategory: Map<String, Double>,
    categories: List<Category>
) {
    val total = expensesByCategory.values.sum()
    val colors = listOf(
        Color(0xFF3B82F6), Color(0xFF10B981), Color(0xFFF59E0B),
        Color(0xFFEF4444), Color(0xFF8B5CF6), Color(0xFFEC4899),
        Color(0xFF06B6D4), Color(0xFF84CC16), Color(0xFFF97316)
    )

    Column {
        // Pie Chart
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = minOf(size.width, size.height) / 3
            
            var startAngle = 0f
            expensesByCategory.entries.forEachIndexed { index, (categoryId, amount) ->
                val sweepAngle = (amount / total * 360).toFloat()
                val color = colors[index % colors.size]
                
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )
                startAngle += sweepAngle
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            expensesByCategory.entries.forEachIndexed { index, (categoryId, amount) ->
                val category = categories.find { it.id == categoryId }
                val percentage = (amount / total * 100).toInt()
                val color = colors[index % colors.size]
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawCircle(color = color)
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${category?.emoji ?: "💰"} ${category?.name ?: "Unknown"}")
                    }
                    Text("$percentage%")
                }
            }
        }
    }
}
