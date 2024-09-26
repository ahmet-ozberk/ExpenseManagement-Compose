package tosbik.ao.parayonetimi.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tosbik.ao.parayonetimi.common.Constants
import tosbik.ao.parayonetimi.data.repository.CurrentDateRepository
import tosbik.ao.parayonetimi.data.source.local.ExpenseDatabase
import tosbik.ao.parayonetimi.data.source.local.ExpenseTypeConvertor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideExpenseDatabase(
        @ApplicationContext context: Context
    ): ExpenseDatabase {
        return Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            Constants.EXPENSE_DATABASE_NAME
        )
            .addTypeConverter(ExpenseTypeConvertor())
            .fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun provideExpenseDao(
        expenseDatabase: ExpenseDatabase
    ) = expenseDatabase.expenseDao

    @Provides
    @Singleton
    fun provideCurrentDateRepository() : CurrentDateRepository = CurrentDateRepository()

}