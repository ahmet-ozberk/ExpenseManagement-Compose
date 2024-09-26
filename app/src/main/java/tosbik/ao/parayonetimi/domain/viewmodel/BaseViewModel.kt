package tosbik.ao.parayonetimi.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    fun showLoading() {
        _loadingState.value = true
    }

    fun hideLoading() {
        _loadingState.value = false
    }

    fun showError(message: String) {
        _errorState.value = message
    }

    fun clearError() {
        _errorState.value = null
    }

    protected fun closeDialogs() {
        clearError()
        hideLoading()
    }
}