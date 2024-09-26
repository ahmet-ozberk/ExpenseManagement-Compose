package tosbik.ao.parayonetimi.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import tosbik.ao.parayonetimi.data.model.AppState
import tosbik.ao.parayonetimi.data.repository.CurrentDateRepository
import java.time.LocalDate
import javax.inject.Inject

class CurrentDateUseCase @Inject constructor(private val currentDateRepository: CurrentDateRepository) {
    fun getCurrentDate(): Flow<AppState<LocalDate>> = flow {
        emit(AppState.loading())
        try {
            val date = currentDateRepository.getLocalDate().first()
            emit(AppState.success(date))
        } catch (e: Exception) {
            emit(AppState.error<LocalDate>(e.message ?: "Unknown error"))
        }
    }
    suspend fun saveCurrentDate(date: LocalDate) = currentDateRepository.saveLocalDate(date)
}