package com.dime.android.data.dao

import androidx.room.*
import com.dime.android.data.entity.MainBudget
import kotlinx.coroutines.flow.Flow

@Dao
interface MainBudgetDao {
    @Query("SELECT * FROM main_budgets ORDER BY dateCreated DESC")
    fun getAllMainBudgets(): Flow<List<MainBudget>>

    @Query("SELECT * FROM main_budgets WHERE id = :id")
    suspend fun getMainBudgetById(id: String): MainBudget?

    @Insert
    suspend fun insertMainBudget(mainBudget: MainBudget)

    @Update
    suspend fun updateMainBudget(mainBudget: MainBudget)

    @Delete
    suspend fun deleteMainBudget(mainBudget: MainBudget)

    @Query("DELETE FROM main_budgets")
    suspend fun deleteAllMainBudgets()
}
