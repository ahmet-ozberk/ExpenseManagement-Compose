package tosbik.ao.parayonetimi.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tosbik.ao.parayonetimi.ui.theme.AppColor

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    border: BorderStroke? = null,
    bgColor: Color = AppColor.Primary,
    contentColor: Color = Color.White,
    content: @Composable () -> Unit
) {
    ElevatedButton(
        onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = bgColor,
            contentColor = contentColor,
        ),
        border = border,
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
    ) {
        content()
    }
}