package tosbik.ao.parayonetimi.ui.components

import android.os.Build
import android.os.VibrationEffect
import android.os.VibrationEffect.EFFECT_CLICK
import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tosbik.ao.parayonetimi.R
import tosbik.ao.parayonetimi.ui.theme.AppColor
import tosbik.ao.parayonetimi.ui.theme.AppFontFamily

enum class CustomKeyboardKey(val label: String) {
    NUMBER_0("0"),
    NUMBER_1("1"),
    NUMBER_2("2"),
    NUMBER_3("3"),
    NUMBER_4("4"),
    NUMBER_5("5"),
    NUMBER_6("6"),
    NUMBER_7("7"),
    NUMBER_8("8"),
    NUMBER_9("9"),
    ADD("ADD"),
    DELETE("DEL")
}

@Composable
fun CustomKeyboard(onKeyPress: (CustomKeyboardKey) -> Unit) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            for (key in listOf(
                CustomKeyboardKey.NUMBER_1,
                CustomKeyboardKey.NUMBER_2,
                CustomKeyboardKey.NUMBER_3
            )) {
                KeyboardButton(
                    modifier = Modifier.weight(1f),
                    key = key,
                    onClick = { onKeyPress(key) })
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            for (key in listOf(
                CustomKeyboardKey.NUMBER_4,
                CustomKeyboardKey.NUMBER_5,
                CustomKeyboardKey.NUMBER_6
            )) {
                KeyboardButton(
                    modifier = Modifier.weight(1f),
                    key = key,
                    onClick = { onKeyPress(key) })
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            for (key in listOf(
                CustomKeyboardKey.NUMBER_7,
                CustomKeyboardKey.NUMBER_8,
                CustomKeyboardKey.NUMBER_9
            )) {
                KeyboardButton(
                    modifier = Modifier.weight(1f),
                    key = key,
                    onClick = { onKeyPress(key) })
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            KeyboardButton(
                modifier = Modifier.weight(1f),
                key = CustomKeyboardKey.NUMBER_0,
                onClick = { onKeyPress(CustomKeyboardKey.NUMBER_0) })
            KeyboardButton(
                modifier = Modifier.weight(1f),
                key = CustomKeyboardKey.DELETE,
                onClick = {
                    onKeyPress(CustomKeyboardKey.DELETE)
                },
                bgColor = AppColor.ExpenseHard,
                frColor = AppColor.White
            )
            KeyboardButton(modifier = Modifier.weight(1f), key = CustomKeyboardKey.ADD, onClick = {
                onKeyPress(CustomKeyboardKey.ADD)
            }, bgColor = AppColor.IncomeHard, frColor = AppColor.White)


        }
    }
}

@Composable
fun KeyboardButton(
    modifier: Modifier = Modifier,
    bgColor: Color = AppColor.Coriander,
    frColor: Color = AppColor.Black,
    key: CustomKeyboardKey,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val vibrator = context.getSystemService("vibrator") as Vibrator
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        vibrator.vibrate(
                            VibrationEffect.createPredefined(EFFECT_CLICK)
                        )
                    }
                onClick()
            }
            .height((screenHeight*0.06).dp)
            .background(bgColor)
            .border(0.2.dp, AppColor.DiffBackgroundLight, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        AppText(
            text = when (key) {
                CustomKeyboardKey.DELETE -> stringResource(R.string.delete)
                CustomKeyboardKey.ADD -> stringResource(R.string.new_expense_add)
                else -> key.label
            },
            color = frColor,
            fontSize = when (key) {
                CustomKeyboardKey.DELETE -> 16f
                CustomKeyboardKey.ADD -> 16f
                else -> 24f
            }.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontFamily = AppFontFamily.jetbrainsMono
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomKeyboardPreview() {
    CustomKeyboard(
        onKeyPress = { key -> println("Key pressed: $key") },
    )
}