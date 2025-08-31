package com.dime.android.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Transaction(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val amount: Double = 0.0,
    val note: String = "",
    val date: Date = Date(),
    val day: Date = Date(),
    val month: Date = Date(),
    val categoryId: String,
    val income: Boolean = false,
    val onceRecurring: Boolean = false,
    val recurringType: Int = 0, // 0: none, 1: daily, 2: weekly, 3: monthly
    val recurringCoefficient: Int = 1,
    val nextTransactionDate: Date? = null
)
