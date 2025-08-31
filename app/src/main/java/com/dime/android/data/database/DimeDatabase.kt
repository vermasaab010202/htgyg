package com.dime.android.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.dime.android.data.converter.DateConverter
import com.dime.android.data.dao.*
import com.dime.android.data.entity.*

@Database(
    entities = [Transaction::class, Category::class, Budget::class, MainBudget::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class DimeDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
    abstract fun mainBudgetDao(): MainBudgetDao

    companion object {
        @Volatile
        private var INSTANCE: DimeDatabase? = null

        fun getDatabase(context: Context): DimeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DimeDatabase::class.java,
                    "dime_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
