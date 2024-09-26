package tosbik.ao.parayonetimi.data.source.local

import android.os.Build
import androidx.annotation.StringRes
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import tosbik.ao.parayonetimi.ui.home.ExpenseType
import tosbik.ao.parayonetimi.ui.newexpense.RepeatTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@ProvidedTypeConverter
class ExpenseTypeConvertor {

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let {
            LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE)
        }
    }

    @TypeConverter
    fun fromRepeatTime(repeatTime: RepeatTime?): Int? {
        return repeatTime?.title
    }

    @TypeConverter
    fun toRepeatTime(@StringRes title: Int?): RepeatTime? {
        return title?.let {
            RepeatTime.entries.find { it.title == title }
        }
    }

    @TypeConverter
    fun fromExpenseType(expenseType: ExpenseType?): String {
        return when (expenseType) {
            is ExpenseType.Income -> "Income"
            is ExpenseType.Expense -> "Expense"
            null -> ""
        }
    }

    @TypeConverter
    fun toExpenseType(expenseTypeString: String?): ExpenseType? {
        return when (expenseTypeString) {
            "Income" -> ExpenseType.Income
            "Expense" -> ExpenseType.Expense
            else -> null
        }
    }
}