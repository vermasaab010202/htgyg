package com.dime.android.data.repository

import com.dime.android.data.dao.*
import com.dime.android.data.entity.*
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DimeRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    private val budgetDao: BudgetDao,
    private val mainBudgetDao: MainBudgetDao
) {
    // Transaction operations
    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()
    
    fun getTransactionsByType(isIncome: Boolean): Flow<List<Transaction>> = 
        transactionDao.getTransactionsByType(isIncome)
    
    fun getTransactionsByCategory(categoryId: String): Flow<List<Transaction>> = 
        transactionDao.getTransactionsByCategory(categoryId)
    
    fun getTransactionsByDateRange(startDate: Date, endDate: Date): Flow<List<Transaction>> = 
        transactionDao.getTransactionsByDateRange(startDate, endDate)
    
    suspend fun insertTransaction(transaction: Transaction) = transactionDao.insertTransaction(transaction)
    
    suspend fun updateTransaction(transaction: Transaction) = transactionDao.updateTransaction(transaction)
    
    suspend fun deleteTransaction(transaction: Transaction) = transactionDao.deleteTransaction(transaction)
    
    suspend fun getTotalAmountByTypeAndDateRange(isIncome: Boolean, startDate: Date, endDate: Date): Double = 
        transactionDao.getTotalAmountByTypeAndDateRange(isIncome, startDate, endDate) ?: 0.0

    // Category operations
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()
    
    fun getCategoriesByType(isIncome: Boolean): Flow<List<Category>> = 
        categoryDao.getCategoriesByType(isIncome)
    
    suspend fun getCategoryById(id: String): Category? = categoryDao.getCategoryById(id)
    
    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)
    
    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)
    
    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)

    // Budget operations
    fun getAllBudgets(): Flow<List<Budget>> = budgetDao.getAllBudgets()
    
    suspend fun getBudgetByCategory(categoryId: String): Budget? = budgetDao.getBudgetByCategory(categoryId)
    
    suspend fun insertBudget(budget: Budget) = budgetDao.insertBudget(budget)
    
    suspend fun updateBudget(budget: Budget) = budgetDao.updateBudget(budget)
    
    suspend fun deleteBudget(budget: Budget) = budgetDao.deleteBudget(budget)

    // Main Budget operations
    fun getAllMainBudgets(): Flow<List<MainBudget>> = mainBudgetDao.getAllMainBudgets()
    
    suspend fun insertMainBudget(mainBudget: MainBudget) = mainBudgetDao.insertMainBudget(mainBudget)
    
    suspend fun updateMainBudget(mainBudget: MainBudget) = mainBudgetDao.updateMainBudget(mainBudget)
    
    suspend fun deleteMainBudget(mainBudget: MainBudget) = mainBudgetDao.deleteMainBudget(mainBudget)

    // Utility operations
    suspend fun deleteAllData() {
        transactionDao.deleteAllTransactions()
        budgetDao.deleteAllBudgets()
        mainBudgetDao.deleteAllMainBudgets()
        categoryDao.deleteAllCategories()
    }
}
