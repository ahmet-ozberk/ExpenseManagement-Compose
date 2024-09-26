package tosbik.ao.parayonetimi.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tosbik.ao.parayonetimi.domain.model.Expense

@Database(entities = [Expense::class], version = 1, exportSchema = false)
@TypeConverters(ExpenseTypeConvertor::class)
abstract class ExpenseDatabase: RoomDatabase() {

    abstract val expenseDao: ExpenseDao

}