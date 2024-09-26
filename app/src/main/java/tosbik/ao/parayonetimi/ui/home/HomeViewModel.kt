package tosbik.ao.parayonetimi.ui.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tosbik.ao.parayonetimi.data.model.AppState
import tosbik.ao.parayonetimi.domain.model.Expense
import tosbik.ao.parayonetimi.domain.usecase.CurrentDateUseCase
import tosbik.ao.parayonetimi.domain.usecase.ExpenseUseCase
import tosbik.ao.parayonetimi.domain.viewmodel.BaseViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val expenseUseCase: ExpenseUseCase,
    private val currentDateUseCase: CurrentDateUseCase
) :BaseViewModel() {
    private val _expensesState = MutableStateFlow<AppState<List<Expense>>>(AppState.loading())
    val expensesState: StateFlow<AppState<List<Expense>>> = _expensesState.asStateFlow()

    private val _totalAmountState = MutableStateFlow<AppState<Double>>(AppState.loading())
    val totalAmountState: StateFlow<AppState<Double>> = _totalAmountState.asStateFlow()

    private val _currentDateState = MutableStateFlow<AppState<LocalDate>>(AppState.loading())
    val currentDateState: StateFlow<AppState<LocalDate>> = _currentDateState.asStateFlow()

    init {
        initial()
    }

    fun initial(){
        getAllExpenses()
        getTotalAmount()
    }

    fun saveCurrentDate(date: LocalDate) {
        viewModelScope.launch {
            currentDateUseCase.saveCurrentDate(date)
            getTotalAmount()
        }
    }

    private fun getAllExpenses() {
        closeDialogs()
        viewModelScope.launch {
            expenseUseCase.getAllExpenses()
                .collect { state ->
                    when (state) {
                        is AppState.Success -> {
                            _expensesState.value = state
                        }
                        else -> _expensesState.value = state
                    }
                }
        }
    }

    fun deleteExpense(expense: Expense) {
        showLoading()
        viewModelScope.launch {
            when (val result = expenseUseCase.deleteExpense(expense)) {
                is AppState.Success -> initial()
                is AppState.Error -> {
                    hideLoading()
                    showError(result.message)
                }
                is AppState.Loading -> {}
            }
        }
    }


    private fun getTotalAmount() {
        viewModelScope.launch {
            currentDateUseCase.getCurrentDate()
                .collect { state ->
                    _currentDateState.value = state
                    if (state is AppState.Success) {
                        val totalAmount = expenseUseCase.getTotalExpense(state.data)
                        _totalAmountState.value = AppState.success(totalAmount)
                    }
                }
        }
    }
}