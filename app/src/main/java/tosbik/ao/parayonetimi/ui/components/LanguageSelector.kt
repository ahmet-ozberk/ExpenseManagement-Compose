package tosbik.ao.parayonetimi.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tosbik.ao.parayonetimi.R
import tosbik.ao.parayonetimi.common.LocalizationUtils
import tosbik.ao.parayonetimi.ui.theme.AppColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelector(
    isTextButton: Boolean = true,
    contentAlignment: Alignment = Alignment.Center,
    iconColor: Color = AppColor.Primary,
    iconSize: Int = 24
) {
    val languages = listOf(("tr" to "Türkçe"), ("en" to "English"))
    val context = LocalContext.current
    var selectedLanguage by remember { mutableStateOf("tr") }
    val showState = remember { mutableStateOf(false) }
    Box(contentAlignment = contentAlignment) {
        if (isTextButton) {
            AppTextButton(onClick = {
                showState.value = true
            }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.language),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = AppColor.Primary
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    AppText(
                        text = stringResource(R.string.language),
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    AppText(
                        text = languages.first { it.first == LocalizationUtils.getAppLocale() }.second,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                    )
                }
            }
        } else {
            IconButton(onClick = {
                showState.value = true
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.language),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize.dp),
                    tint = iconColor
                )
            }
        }
        if (showState.value) {
            ModalBottomSheet(
                onDismissRequest = { showState.value = false },
                containerColor = AppColor.DiffBackground,
                shape = RoundedCornerShape(4.dp),
                contentWindowInsets = { WindowInsets.ime }
            ) {
                LazyColumn {
                    languages.forEach { (key, value) ->
                        item {
                            Row(horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        LocalizationUtils.setAppLocale(context, key)
                                        selectedLanguage = key
                                        showState.value = false
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)) {
                                AppText(
                                    text = value,
                                    color = if (LocalizationUtils.getAppLocale() == key)
                                        AppColor.PrimaryHard
                                    else AppColor.BlueGrey400,
                                )
                                if (LocalizationUtils.getAppLocale() == key) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = AppColor.PrimaryHard,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LanguageSelectorPreview() {
    LanguageSelector()
}