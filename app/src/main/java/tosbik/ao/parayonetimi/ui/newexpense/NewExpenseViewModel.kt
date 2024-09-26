package tosbik.ao.parayonetimi.ui.newexpense

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tosbik.ao.parayonetimi.MainApplication
import tosbik.ao.parayonetimi.R
import tosbik.ao.parayonetimi.data.model.AppState
import tosbik.ao.parayonetimi.domain.model.Expense
import tosbik.ao.parayonetimi.domain.usecase.ExpenseUseCase
import tosbik.ao.parayonetimi.domain.viewmodel.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class NewExpenseViewModel @Inject constructor(
    private val expenseUseCase: ExpenseUseCase
) : BaseViewModel() {


    private val _isAddExpenseComplete = MutableStateFlow(false)
    val isAddExpenseComplete: StateFlow<Boolean> = _isAddExpenseComplete.asStateFlow()


    fun addExpense(expense: Expense) {
        if (expense.name.isBlank()) {
            showError(MainApplication
                    .applicationContext()
                    .resources
                    .getString(R.string.empty_fields))
        } else {
            showLoading()
            viewModelScope.launch {
                when (val result = expenseUseCase.addExpense(expense)) {
                    is AppState.Success -> {
                        hideLoading()
                        _isAddExpenseComplete.update { true }
                    }
                    is AppState.Error -> {
                        hideLoading()
                        _isAddExpenseComplete.update { false }
                        showError(result.message)
                    }
                    is AppState.Loading -> {}
                }
            }
        }
    }

    fun updateExpense(expense: Expense){
        if (expense.name.isBlank()) {
            showError(
                MainApplication
                    .applicationContext()
                    .resources
                    .getString(R.string.empty_fields)
            )
        } else {
            showLoading()
            viewModelScope.launch {
                when (val result = expenseUseCase.updateExpense(expense)) {
                    is AppState.Success -> {
                        hideLoading()
                        _isAddExpenseComplete.update { true }
                    }
                    is AppState.Error -> {
                        hideLoading()
                        _isAddExpenseComplete.update { false }
                        showError(result.message)
                    }
                    is AppState.Loading -> {}
                }
            }
        }
    }
}