package com.dime.android.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "budgets",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Budget(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val amount: Double = 0.0,
    val categoryId: String,
    val type: Int = 0, // 0: monthly, 1: weekly, 2: daily
    val green: Boolean = false,
    val startDate: Date = Date(),
    val dateCreated: Date = Date()
)
