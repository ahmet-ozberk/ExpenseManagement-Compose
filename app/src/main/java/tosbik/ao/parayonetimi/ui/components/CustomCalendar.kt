package tosbik.ao.parayonetimi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import tosbik.ao.parayonetimi.R
import tosbik.ao.parayonetimi.common.LocalizationUtils
import tosbik.ao.parayonetimi.ui.theme.AppColor
import java.time.LocalDate
import java.time.format.TextStyle

@Composable
fun CustomCalendarDialog(
    setShowDialog: (Boolean) -> Unit,
    onSelectedDate: ((LocalDate) -> Unit)? = null
) {
    Popup(
        onDismissRequest = { setShowDialog(false) }, properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            excludeFromSystemGesture = true,
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { setShowDialog(false) },
            contentAlignment = Alignment.BottomCenter
        ) {
            CustomCalendar(onSelectedDate)
        }
    }
}


@Composable
fun CustomCalendar(onSelectedDate: ((LocalDate) -> Unit)? = null) {
    val selectedDate = remember { mutableStateOf<LocalDate>(LocalDate.now()) }
    val currentMonthYear = remember {
        mutableStateOf<Pair<String, Int>>(
            Pair(
                LocalizationUtils.getCurrentMonthNameAndYear(textStyle = TextStyle.FULL).first,
                LocalizationUtils.getCurrentMonthNameAndYear().second
            )
        )
    }
    val activeMonthYear = remember { mutableStateOf(LocalizationUtils.getCurrentDate()) }
    Box(
        modifier = Modifier
            .offset(y = (-100).dp, x = (-24).dp)
            .padding(horizontal = 16.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
            .width(((44 * 7) + 16).dp)
            .clip(RoundedCornerShape(12.dp))
            .background(AppColor.DiffBackgroundLight)
            .padding(horizontal = 8.dp)

    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    activeMonthYear.value = LocalizationUtils.previousDate(activeMonthYear.value)
                    currentMonthYear.value = LocalizationUtils.getDateToStringMY(
                        date = activeMonthYear.value, textStyle = TextStyle.FULL
                    )
                }) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_left),
                        contentDescription = "Previous Month",
                        tint = AppColor.LunarGreen
                    )
                }
                AppText(
                    text = "${currentMonthYear.value.first} ${currentMonthYear.value.second}",
                    color = AppColor.LunarGreen,
                    fontWeight = FontWeight.SemiBold,
                )
                IconButton(onClick = {
                    activeMonthYear.value = LocalizationUtils.nextDate(activeMonthYear.value)
                    currentMonthYear.value = LocalizationUtils.getDateToStringMY(
                        date = activeMonthYear.value, textStyle = TextStyle.FULL
                    )
                }) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_right),
                        contentDescription = "Previous Month",
                        tint = AppColor.LunarGreen
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LocalizationUtils.getShortDayNames().forEach { day ->
                    AppText(
                        text = day,
                        color = AppColor.GradientTextGrey,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                val getWeekCount = LocalizationUtils.getWeeksInMonth(activeMonthYear.value)
                for (i in 1..getWeekCount) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                    ) {
                        for (j in 1..7) {
                            val index = (i - 1) * 7 + j
                            val day = LocalizationUtils.getIndexedDay(
                                index - 1,
                                activeMonthYear.value.year,
                                activeMonthYear.value.monthValue
                            )

                            Box(modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedDate.value = day
                                    onSelectedDate?.invoke(day)
                                }
                                .clip(RoundedCornerShape(8.dp))
                                .height(32.dp)
                                .background(
                                    if (selectedDate.value == day) AppColor.WillowGrove else Color.Transparent
                                ), contentAlignment = Alignment.Center) {
                                AppText(
                                    text = "${day.dayOfMonth}",
                                    color = if (selectedDate.value == day) AppColor.DiffBackgroundLight else if (day.month == activeMonthYear.value.month) AppColor.LunarGreen else AppColor.TabBackgroundColor,
                                    textAlign = TextAlign.Center,
                                    fontWeight = if (day.month == activeMonthYear.value.month) FontWeight.SemiBold else FontWeight.Medium,
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CustomCalendarPreview() {
    CustomCalendar()
}
