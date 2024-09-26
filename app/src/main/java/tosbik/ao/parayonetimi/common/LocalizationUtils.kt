package tosbik.ao.parayonetimi.common

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import tosbik.ao.parayonetimi.MainApplication
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

object LocalizationUtils {
    fun setAppLocale(context: Context, language: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java)
                .applicationLocales = LocaleList.forLanguageTags(language)
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
        }
    }

    fun getAppLocale(): String {
        val context = MainApplication.applicationContext()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.getSystemService(LocaleManager::class.java).applicationLocales.isEmpty.not()) {
                context.getSystemService(LocaleManager::class.java)
                    .applicationLocales[0].toLanguageTag()
            } else {
                "tr"
            }
        } else {
            if (AppCompatDelegate.getApplicationLocales().isEmpty.not() && AppCompatDelegate.getApplicationLocales()[0] != null) {
                AppCompatDelegate.getApplicationLocales()[0]!!.toLanguageTag()
            } else {
                "tr"
            }
        }
    }

    fun getLocale(): Locale {
        return Locale(getAppLocale())
    }

    fun getCurrentMonthNameAndYear(textStyle: TextStyle = TextStyle.SHORT): Pair<String, Int> {
        val currentDate = LocalDate.now()

        val monthName = currentDate.month.getDisplayName(textStyle, getLocale())
        return Pair(monthName, currentDate.year)
    }

    fun getDateToStringMY(
        date: LocalDate,
        textStyle: TextStyle = TextStyle.SHORT
    ): Pair<String, Int> {
        val monthName = date.month.getDisplayName(textStyle, getLocale())
        return Pair(monthName, date.year)
    }

    fun getDateToStringDMY(
        date: LocalDate,
        textStyle: TextStyle = TextStyle.SHORT
    ): Pair<String, Int> {
        val monthName = date.month.getDisplayName(textStyle, getLocale())
        val dayName = date.dayOfMonth
        return Pair("$dayName $monthName", date.year)
    }

    fun getCurrentDate(): LocalDate {
        val currentDate = LocalDate.now()
        return currentDate
    }

    fun getShortDayNames(): List<String> {
        return DayOfWeek.entries.map { dayOfWeek ->
            dayOfWeek.getDisplayName(TextStyle.SHORT, getLocale())
        }
    }

    fun nextDate(date: LocalDate): LocalDate {
        return date.plusMonths(1)
    }

    fun previousDate(date: LocalDate): LocalDate {
        return date.minusMonths(1)
    }


    fun getWeeksInMonth(date: LocalDate): Int {
        val firstDayOfMonth = date.withDayOfMonth(1)
        val lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth())
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value
        val lastDayOfWeek = lastDayOfMonth.dayOfWeek.value
        val daysInMonth = date.lengthOfMonth()
        val daysInFirstWeek = 7 - firstDayOfWeek
        val daysInLastWeek = lastDayOfWeek + 1
        val daysInMiddleWeeks = daysInMonth - daysInFirstWeek - daysInLastWeek
        return if (daysInMiddleWeeks % 7 == 0) {
            2 + (daysInMiddleWeeks / 7)
        } else {
            3 + (daysInMiddleWeeks / 7)
        }
    }


    fun isEqualDate(date1: LocalDate, date2: LocalDate): Boolean {
        return date1.year == date2.year && date1.month == date2.month
    }


    private fun generateCalendarDays(year: Int, month: Int): List<LocalDate> {
        val currentMonth =
            YearMonth.of(year, month)
        val firstDayOfMonth = currentMonth.atDay(1)

        val lastDayOfMonth = currentMonth.atEndOfMonth()

        val days = mutableListOf<LocalDate>()

        val firstDayOfWeekInMonth =
            firstDayOfMonth.dayOfWeek.value
        val daysFromPreviousMonth = if (firstDayOfWeekInMonth != DayOfWeek.MONDAY.value) {
            firstDayOfWeekInMonth - DayOfWeek.MONDAY.value
        } else {
            0
        }

        if (daysFromPreviousMonth > 0) {
            val previousMonth = currentMonth.minusMonths(1)
            val lastDayOfPreviousMonth = previousMonth.atEndOfMonth()
            for (i in daysFromPreviousMonth downTo 1) {
                days.add(lastDayOfPreviousMonth.minusDays((i - 1).toLong()))
            }
        }

        for (day in firstDayOfMonth.dayOfMonth..lastDayOfMonth.dayOfMonth) {
            days.add(firstDayOfMonth.withDayOfMonth(day))
        }

        val remainingDaysInWeek = 7 - lastDayOfMonth.dayOfWeek.value
        if (remainingDaysInWeek > 0) {
            val nextMonth = currentMonth.plusMonths(1)
            for (i in 1..remainingDaysInWeek) {
                days.add(nextMonth.atDay(i))
            }
        }

        return days
    }

    fun getIndexedDay(index: Int, year: Int, month: Int): LocalDate {
        return generateCalendarDays(year, month)[index]
    }
}