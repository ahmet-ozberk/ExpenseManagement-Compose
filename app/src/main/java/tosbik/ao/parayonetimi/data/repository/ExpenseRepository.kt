package tosbik.ao.parayonetimi.data.repository

import tosbik.ao.parayonetimi.data.source.local.ExpenseDao
import tosbik.ao.parayonetimi.domain.model.Expense
import tosbik.ao.parayonetimi.ui.home.ExpenseType
import javax.inject.Inject

class ExpenseRepository @Inject constructor(private val expenseDao: ExpenseDao) {

    fun getExpenses() = expenseDao.getAll()

    suspend fun insertExpense(expense: Expense) = expenseDao.add(expense)

    suspend fun updateExpense(expense: Expense) = expenseDao.update(expense)

    suspend fun deleteExpense(expense: Expense) = expenseDao.delete(expense)

    suspend fun deleteAll() = expenseDao.deleteAll()

}