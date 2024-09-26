package tosbik.ao.parayonetimi.domain.usecase

import android.os.Build
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import tosbik.ao.parayonetimi.common.LocalizationUtils
import tosbik.ao.parayonetimi.data.model.AppState
import tosbik.ao.parayonetimi.data.repository.ExpenseRepository
import tosbik.ao.parayonetimi.domain.model.Expense
import java.time.LocalDate
import javax.inject.Inject

class ExpenseUseCase @Inject constructor(private val expenseRepository: ExpenseRepository) {
    suspend fun deleteAll() : AppState<Unit> {
        return try {
            expenseRepository.deleteAll()
            AppState.success(Unit)
        } catch (e: Exception) {
            AppState.error(e.message ?: "Failed to delete all expenses")
        }
    }

    fun getAllExpenses(): Flow<AppState<List<Expense>>> = flow {
        emit(AppState.loading())
        try {
            val expenses = expenseRepository.getExpenses().first()
            emit(AppState.success(expenses))
        } catch (e: Exception) {
            emit(AppState.error<List<Expense>>(e.message ?: "Unknown error"))
        }
    }

    suspend fun addExpense(expense: Expense): AppState<Unit> {
        return try {
            expenseRepository.insertExpense(expense)
            AppState.success(Unit)
        } catch (e: Exception) {
            AppState.error(e.message ?: "Failed to add expense")
        }
    }

    suspend fun updateExpense(expense: Expense): AppState<Unit> {
        return try {
            expenseRepository.updateExpense(expense)
            AppState.success(Unit)
        } catch (e: Exception) {
            AppState.error(e.message ?: "Failed to update expense")
        }
    }

    suspend fun deleteExpense(expense: Expense): AppState<Unit> {
        return try {
            expenseRepository.deleteExpense(expense)
            AppState.success(Unit)
        } catch (e: Exception) {
            AppState.error(e.message ?: "Failed to delete expense")
        }
    }

    suspend fun getTotalExpense(date: LocalDate): Double {
        val expenses = expenseRepository.getExpenses().first()
            .filter { LocalizationUtils.isEqualDate(date1 = it.date, date2 = date) }
        return expenses.sumOf { it.amount }
    }

}