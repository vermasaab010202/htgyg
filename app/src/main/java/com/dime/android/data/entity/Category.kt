package com.dime.android.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val emoji: String,
    val colour: String,
    val income: Boolean = false,
    val dateCreated: Date = Date(),
    val order: Int = 0
)
