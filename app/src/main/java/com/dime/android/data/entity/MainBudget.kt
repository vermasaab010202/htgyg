package com.dime.android.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "main_budgets")
data class MainBudget(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val amount: Double = 0.0,
    val type: Int = 0, // 0: monthly, 1: weekly, 2: daily
    val startDate: Date = Date(),
    val dateCreated: Date = Date()
)
