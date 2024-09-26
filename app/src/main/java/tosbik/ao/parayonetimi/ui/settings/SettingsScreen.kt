package tosbik.ao.parayonetimi.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import tosbik.ao.parayonetimi.R
import tosbik.ao.parayonetimi.ui.components.AppText
import tosbik.ao.parayonetimi.ui.components.AppTextButton
import tosbik.ao.parayonetimi.ui.components.LanguageSelector
import tosbik.ao.parayonetimi.ui.theme.AppColor

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onDeleteAccount: () -> Unit = {},
) {
    val deleteAccountDialog = remember { mutableStateOf(false) }

    if (deleteAccountDialog.value) {
        AlertDialog(
            onDismissRequest = { deleteAccountDialog.value = false },
            containerColor = AppColor.DiffBackground,
            shape = RoundedCornerShape(4.dp),
            title = {
                AppText(
                    text = stringResource(R.string.delete_account),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColor.DarkGrey
                )
            },
            text = {
                AppText(
                    text = stringResource(R.string.delete_account_info),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = AppColor.DarkGrey
                )
            },
            confirmButton = {
                AppTextButton(
                    onClick = {
                        deleteAccountDialog.value = false
                        viewModel.deleteAllAccountData()
                        onDeleteAccount()
                    },
                ) {
                    AppText(
                        text = stringResource(R.string.delete),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AppColor.ExpenseRed,
                    )
                }
            },
            dismissButton = {
                AppTextButton(
                    onClick = { deleteAccountDialog.value = false },
                ) {
                    AppText(
                        text = stringResource(R.string.cancel),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = AppColor.DarkGrey,
                    )
                }
            }
        )
    }
    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        LanguageSelector()
        Spacer(modifier = Modifier.height(4.dp))
        AppTextButton(
            onClick = { deleteAccountDialog.value = true },
            modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth()
        ) {
            AppText(
                text = stringResource(R.string.delete_account),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColor.ExpenseHard,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}