package com.dime.android.util

import android.content.Context
import android.net.Uri
import com.dime.android.data.entity.Budget
import com.dime.android.data.entity.Category
import com.dime.android.data.entity.MainBudget
import com.dime.android.data.entity.Transaction
import com.dime.android.data.repository.DimeRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class ExportData(
    val transactions: List<TransactionExport>,
    val categories: List<CategoryExport>,
    val budgets: List<BudgetExport>,
    val mainBudgets: List<MainBudgetExport>,
    val exportDate: String,
    val version: String = "1.0"
)

@Serializable
data class TransactionExport(
    val id: String,
    val amount: Double,
    val note: String,
    val dateCreated: String,
    val categoryId: String,
    val recurring: Boolean,
    val recurringFrequency: String?
)

@Serializable
data class CategoryExport(
    val id: String,
    val name: String,
    val emoji: String,
    val colour: String,
    val income: Boolean,
    val dateCreated: String,
    val order: Int
)

@Serializable
data class BudgetExport(
    val id: String,
    val amount: Double,
    val categoryId: String,
    val budgetType: String,
    val sustainable: Boolean,
    val startDate: String,
    val endDate: String
)

@Serializable
data class MainBudgetExport(
    val id: String,
    val amount: Double,
    val budgetType: String,
    val startDate: String,
    val endDate: String
)

@Singleton
class ExportImportManager @Inject constructor(
    private val repository: DimeRepository
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    private val json = Json { 
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    suspend fun exportData(): ExportData {
        val transactions = repository.getAllTransactions().first().map { transaction ->
            TransactionExport(
                id = transaction.id,
                amount = transaction.amount,
                note = transaction.note,
                dateCreated = dateFormat.format(transaction.dateCreated),
                categoryId = transaction.categoryId,
                recurring = transaction.recurring,
                recurringFrequency = transaction.recurringFrequency
            )
        }

        val categories = repository.getAllCategories().first().map { category ->
            CategoryExport(
                id = category.id,
                name = category.name,
                emoji = category.emoji,
                colour = category.colour,
                income = category.income,
                dateCreated = dateFormat.format(category.dateCreated),
                order = category.order
            )
        }

        val budgets = repository.getAllBudgets().first().map { budget ->
            BudgetExport(
                id = budget.id,
                amount = budget.amount,
                categoryId = budget.categoryId,
                budgetType = budget.budgetType,
                sustainable = budget.sustainable,
                startDate = dateFormat.format(budget.startDate),
                endDate = dateFormat.format(budget.endDate)
            )
        }

        val mainBudgets = repository.getAllMainBudgets().first().map { mainBudget ->
            MainBudgetExport(
                id = mainBudget.id,
                amount = mainBudget.amount,
                budgetType = mainBudget.budgetType,
                startDate = dateFormat.format(mainBudget.startDate),
                endDate = dateFormat.format(mainBudget.endDate)
            )
        }

        return ExportData(
            transactions = transactions,
            categories = categories,
            budgets = budgets,
            mainBudgets = mainBudgets,
            exportDate = dateFormat.format(Date())
        )
    }

    suspend fun exportToJson(context: Context, uri: Uri): Result<String> {
        return try {
            val exportData = exportData()
            val jsonString = json.encodeToString(exportData)
            
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(jsonString.toByteArray())
            }
            
            Result.success("Data exported successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun importFromJson(context: Context, uri: Uri): Result<String> {
        return try {
            val jsonString = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes().toString(Charsets.UTF_8)
            } ?: return Result.failure(Exception("Could not read file"))

            val exportData = json.decodeFromString<ExportData>(jsonString)
            
            // Import categories first (needed for transactions and budgets)
            exportData.categories.forEach { categoryExport ->
                val category = Category(
                    id = categoryExport.id,
                    name = categoryExport.name,
                    emoji = categoryExport.emoji,
                    colour = categoryExport.colour,
                    income = categoryExport.income,
                    dateCreated = dateFormat.parse(categoryExport.dateCreated) ?: Date(),
                    order = categoryExport.order
                )
                repository.insertCategory(category)
            }

            // Import transactions
            exportData.transactions.forEach { transactionExport ->
                val transaction = Transaction(
                    id = transactionExport.id,
                    amount = transactionExport.amount,
                    note = transactionExport.note,
                    dateCreated = dateFormat.parse(transactionExport.dateCreated) ?: Date(),
                    categoryId = transactionExport.categoryId,
                    recurring = transactionExport.recurring,
                    recurringFrequency = transactionExport.recurringFrequency
                )
                repository.insertTransaction(transaction)
            }

            // Import budgets
            exportData.budgets.forEach { budgetExport ->
                val budget = Budget(
                    id = budgetExport.id,
                    amount = budgetExport.amount,
                    categoryId = budgetExport.categoryId,
                    budgetType = budgetExport.budgetType,
                    sustainable = budgetExport.sustainable,
                    startDate = dateFormat.parse(budgetExport.startDate) ?: Date(),
                    endDate = dateFormat.parse(budgetExport.endDate) ?: Date()
                )
                repository.insertBudget(budget)
            }

            // Import main budgets
            exportData.mainBudgets.forEach { mainBudgetExport ->
                val mainBudget = MainBudget(
                    id = mainBudgetExport.id,
                    amount = mainBudgetExport.amount,
                    budgetType = mainBudgetExport.budgetType,
                    startDate = dateFormat.parse(mainBudgetExport.startDate) ?: Date(),
                    endDate = dateFormat.parse(mainBudgetExport.endDate) ?: Date()
                )
                repository.insertMainBudget(mainBudget)
            }

            Result.success("Data imported successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun exportToCsv(context: Context, uri: Uri): Result<String> {
        return try {
            val transactions = repository.getAllTransactions().first()
            val categories = repository.getAllCategories().first()
            val categoryMap = categories.associateBy { it.id }

            val csvContent = buildString {
                appendLine("Date,Category,Amount,Note,Type,Recurring")
                transactions.forEach { transaction ->
                    val category = categoryMap[transaction.categoryId]
                    val type = if (category?.income == true) "Income" else "Expense"
                    appendLine("${dateFormat.format(transaction.dateCreated)},${category?.name ?: "Unknown"},${transaction.amount},\"${transaction.note}\",${type},${transaction.recurring}")
                }
            }

            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(csvContent.toByteArray())
            }

            Result.success("CSV exported successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
