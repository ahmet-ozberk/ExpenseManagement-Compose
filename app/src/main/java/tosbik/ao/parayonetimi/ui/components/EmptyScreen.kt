package tosbik.ao.parayonetimi.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tosbik.ao.parayonetimi.R
import tosbik.ao.parayonetimi.ui.theme.AppColor

@Composable
fun EmptyScreen() {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Column(
        modifier = Modifier.fillMaxWidth().height((screenHeight.value*0.7).dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(64.dp),
            imageVector = Icons.Default.Face,
            contentDescription = "Warning",
            tint = AppColor.LunarGreen
        )
        Spacer(modifier = Modifier.size(4.dp))
        AppText(
            text = stringResource(R.string.empty_records),
            color = AppColor.LunarGreen,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, showSystemUi = true)
@Composable
fun EmptyScreenPreview() {
    EmptyScreen()
}