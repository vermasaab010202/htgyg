package com.dime.android.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dime.android.data.entity.Transaction
import com.dime.android.data.repository.DimeRepository
import com.dime.android.util.DateUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.*
import java.util.concurrent.TimeUnit

@HiltWorker
class RecurringTransactionWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: DimeRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            processRecurringTransactions()
            Result.success()
        } catch (exception: Exception) {
            Result.failure()
        }
    }

    private suspend fun processRecurringTransactions() {
        // Get all recurring transactions
        val recurringTransactions = repository.getAllTransactions().first()
            .filter { it.recurring }

        val currentDate = Date()

        recurringTransactions.forEach { transaction ->
            val nextDueDate = calculateNextDueDate(transaction)
            
            // Check if it's time to create a new instance
            if (nextDueDate != null && nextDueDate.before(currentDate) || nextDueDate == currentDate) {
                createRecurringInstance(transaction, nextDueDate)
            }
        }
    }

    private fun calculateNextDueDate(transaction: Transaction): Date? {
        val lastDate = transaction.dateCreated
        val calendar = Calendar.getInstance().apply {
            time = lastDate
        }

        return when (transaction.recurringFrequency) {
            "daily" -> {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                calendar.time
            }
            "weekly" -> {
                calendar.add(Calendar.WEEK_OF_YEAR, 1)
                calendar.time
            }
            "monthly" -> {
                calendar.add(Calendar.MONTH, 1)
                calendar.time
            }
            "yearly" -> {
                calendar.add(Calendar.YEAR, 1)
                calendar.time
            }
            else -> null
        }
    }

    private suspend fun createRecurringInstance(originalTransaction: Transaction, dueDate: Date) {
        // Check if we already created an instance for this date
        val existingTransactions = repository.getTransactionsByDateRange(
            DateUtils.startOfDay(dueDate),
            DateUtils.endOfDay(dueDate)
        ).first()

        val alreadyExists = existingTransactions.any { 
            it.categoryId == originalTransaction.categoryId &&
            it.amount == originalTransaction.amount &&
            it.note == originalTransaction.note &&
            it.recurring
        }

        if (!alreadyExists) {
            val newTransaction = Transaction(
                id = UUID.randomUUID().toString(),
                amount = originalTransaction.amount,
                note = originalTransaction.note,
                dateCreated = dueDate,
                categoryId = originalTransaction.categoryId,
                recurring = true,
                recurringFrequency = originalTransaction.recurringFrequency
            )
            
            repository.insertTransaction(newTransaction)
        }
    }
}
