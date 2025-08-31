package com.dime.android.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.dime.android.data.entity.Category
import com.dime.android.data.entity.Transaction

data class TransactionWithCategory(
    @Embedded val transaction: Transaction,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: Category
)

data class CategoryWithTransactions(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val transactions: List<Transaction>
)

data class BudgetWithCategory(
    @Embedded val budget: com.dime.android.data.entity.Budget,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: Category
)
