package tosbik.ao.parayonetimi.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tosbik.ao.parayonetimi.domain.usecase.ExpenseUseCase
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val expenseUseCase: ExpenseUseCase,
) : ViewModel() {
    fun deleteAllAccountData(){
        viewModelScope.launch {
            expenseUseCase.deleteAll()
        }
    }
}