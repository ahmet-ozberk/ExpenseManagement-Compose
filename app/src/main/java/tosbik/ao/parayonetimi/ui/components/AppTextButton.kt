package tosbik.ao.parayonetimi.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import tosbik.ao.parayonetimi.R
import tosbik.ao.parayonetimi.ui.theme.AppColor

@Composable
fun AppTextButton(
    onClick: () -> Unit,
    textColor: Color = AppColor.Primary,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val isPressed by interactionSource.collectIsPressedAsState()

    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            contentColor = textColor,
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(4.dp),
        interactionSource = interactionSource,
        modifier = modifier
    ) { content() }
}