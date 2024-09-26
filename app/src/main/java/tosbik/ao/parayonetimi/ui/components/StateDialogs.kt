package tosbik.ao.parayonetimi.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tosbik.ao.parayonetimi.R
import tosbik.ao.parayonetimi.ui.theme.AppColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingDialog(onDismiss: () -> Unit) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        content = {
            CircularProgressIndicator()
        }
    )
}

@Composable
fun ErrorDialog(errorMessage: String, onDismiss: () -> Unit) {
    AlertDialog(
        containerColor = AppColor.DiffBackground,
        shape = RoundedCornerShape(4.dp),
        icon = {
            Icon(
                Icons.Rounded.Warning,
                contentDescription = "Example Icon",
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            AppText(text = stringResource(R.string.error_dialog_title))
        },
        text = {
            AppText(text = errorMessage)
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            AppTextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                AppText(stringResource(R.string.close))
            }
        },
    )
}