package tosbik.ao.parayonetimi.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tosbik.ao.parayonetimi.domain.model.Expense

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Update
    suspend fun update(expense: Expense)

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun get(id: Int): Expense?

    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAll(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE expense_type = :type")
    fun getAllByType(type: String): Flow<List<Expense>>

    @Query("DELETE FROM expenses")
    suspend fun deleteAll()
}