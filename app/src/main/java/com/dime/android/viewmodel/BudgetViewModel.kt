package com.dime.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dime.android.data.entity.Budget
import com.dime.android.data.entity.MainBudget
import com.dime.android.data.repository.DimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val repository: DimeRepository
) : ViewModel() {

    private val _budgets = MutableStateFlow<List<Budget>>(emptyList())
    val budgets: StateFlow<List<Budget>> = _budgets.asStateFlow()

    private val _mainBudgets = MutableStateFlow<List<MainBudget>>(emptyList())
    val mainBudgets: StateFlow<List<MainBudget>> = _mainBudgets.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadBudgets()
        loadMainBudgets()
    }

    private fun loadBudgets() {
        viewModelScope.launch {
            repository.getAllBudgets().collect {
                _budgets.value = it
            }
        }
    }

    private fun loadMainBudgets() {
        viewModelScope.launch {
            repository.getAllMainBudgets().collect {
                _mainBudgets.value = it
            }
        }
    }

    fun addBudget(budget: Budget) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.insertBudget(budget)
            _isLoading.value = false
        }
    }

    fun updateBudget(budget: Budget) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.updateBudget(budget)
            _isLoading.value = false
        }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.deleteBudget(budget)
            _isLoading.value = false
        }
    }

    fun addMainBudget(mainBudget: MainBudget) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.insertMainBudget(mainBudget)
            _isLoading.value = false
        }
    }

    fun updateMainBudget(mainBudget: MainBudget) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.updateMainBudget(mainBudget)
            _isLoading.value = false
        }
    }

    suspend fun getBudgetByCategory(categoryId: String): Budget? {
        return repository.getBudgetByCategory(categoryId)
    }
}
