package com.dime.android.di

import android.content.Context
import androidx.room.Room
import com.dime.android.data.database.DimeDatabase
import com.dime.android.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DimeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            DimeDatabase::class.java,
            "dime_database"
        ).build()
    }

    @Provides
    fun provideTransactionDao(database: DimeDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    fun provideCategoryDao(database: DimeDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideBudgetDao(database: DimeDatabase): BudgetDao {
        return database.budgetDao()
    }

    @Provides
    fun provideMainBudgetDao(database: DimeDatabase): MainBudgetDao {
        return database.mainBudgetDao()
    }
}
