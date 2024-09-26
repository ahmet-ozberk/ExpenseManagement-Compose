package tosbik.ao.parayonetimi.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tosbik.ao.parayonetimi.MainApplication
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CurrentDateRepository @Inject constructor() {
    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("current_date")
        private val CURRENT_DATE = stringPreferencesKey("current_date")
    }

    private val context = MainApplication.applicationContext()

    fun getLocalDate(): Flow<LocalDate> {
        val formatter =
            DateTimeFormatter.ISO_LOCAL_DATE
        return context.dataStore.data.map { preferences ->
            val result = preferences[CURRENT_DATE]?.let {
                LocalDate.parse(it, formatter)
            }
            result ?: LocalDate.now()
        }
    }

    suspend fun saveLocalDate(date: LocalDate) {
        val formatter =
            DateTimeFormatter.ISO_LOCAL_DATE
        context.dataStore.edit { preferences ->
            preferences[CURRENT_DATE] = date.format(formatter)
        }
    }
}