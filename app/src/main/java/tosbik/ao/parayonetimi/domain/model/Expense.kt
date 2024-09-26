package tosbik.ao.parayonetimi.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tosbik.ao.parayonetimi.common.CurrencyType
import tosbik.ao.parayonetimi.ui.home.ExpenseType
import tosbik.ao.parayonetimi.ui.newexpense.RepeatTime
import java.time.LocalDate

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val amount: Double,
    val date: LocalDate,
    val repeat: RepeatTime = RepeatTime.OneShot,
    @ColumnInfo(name = "expense_type") val expenseType: ExpenseType,
    val currency: CurrencyType = CurrencyType.TL,
)
