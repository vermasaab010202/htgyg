package com.dime.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dime.android.data.entity.Category
import com.dime.android.data.repository.DimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: DimeRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _expenseCategories = MutableStateFlow<List<Category>>(emptyList())
    val expenseCategories: StateFlow<List<Category>> = _expenseCategories.asStateFlow()

    private val _incomeCategories = MutableStateFlow<List<Category>>(emptyList())
    val incomeCategories: StateFlow<List<Category>> = _incomeCategories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getAllCategories().collect {
                _categories.value = it
            }
        }
        
        viewModelScope.launch {
            repository.getCategoriesByType(false).collect {
                _expenseCategories.value = it
            }
        }
        
        viewModelScope.launch {
            repository.getCategoriesByType(true).collect {
                _incomeCategories.value = it
            }
        }
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.insertCategory(category)
            _isLoading.value = false
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.updateCategory(category)
            _isLoading.value = false
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.deleteCategory(category)
            _isLoading.value = false
        }
    }

    suspend fun getCategoryById(id: String): Category? {
        return repository.getCategoryById(id)
    }
}
