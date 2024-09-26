package tosbik.ao.parayonetimi.ui.newexpense

import dagger.hilt.android.lifecycle.HiltViewModel
import tosbik.ao.parayonetimi.domain.model.Expense
import tosbik.ao.parayonetimi.domain.viewmodel.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor() : BaseViewModel() {
    var selectedExpense: Expense? = null

    fun updateSelectedExpense(expense: Expense) {
        selectedExpense = expense
    }

    fun clearSelectedExpense() {
        selectedExpense = null
    }
}