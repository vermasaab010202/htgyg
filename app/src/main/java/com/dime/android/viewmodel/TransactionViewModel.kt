package com.dime.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dime.android.data.entity.Transaction
import com.dime.android.data.repository.DimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: DimeRepository
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedTransaction = MutableStateFlow<Transaction?>(null)
    val selectedTransaction: StateFlow<Transaction?> = _selectedTransaction.asStateFlow()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            repository.getAllTransactions().collect {
                _transactions.value = it
            }
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.insertTransaction(transaction)
            _isLoading.value = false
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.updateTransaction(transaction)
            _isLoading.value = false
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.deleteTransaction(transaction)
            _isLoading.value = false
        }
    }

    fun selectTransaction(transaction: Transaction?) {
        _selectedTransaction.value = transaction
    }

    fun getTransactionsByType(isIncome: Boolean) {
        viewModelScope.launch {
            repository.getTransactionsByType(isIncome).collect {
                _transactions.value = it
            }
        }
    }

    fun getTransactionsByDateRange(startDate: Date, endDate: Date) {
        viewModelScope.launch {
            repository.getTransactionsByDateRange(startDate, endDate).collect {
                _transactions.value = it
            }
        }
    }

    suspend fun getTotalAmount(isIncome: Boolean, startDate: Date, endDate: Date): Double {
        return repository.getTotalAmountByTypeAndDateRange(isIncome, startDate, endDate)
    }
}
