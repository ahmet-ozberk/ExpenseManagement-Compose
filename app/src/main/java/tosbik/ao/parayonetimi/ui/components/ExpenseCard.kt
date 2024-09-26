package tosbik.ao.parayonetimi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tosbik.ao.parayonetimi.R
import tosbik.ao.parayonetimi.common.LocalizationUtils
import tosbik.ao.parayonetimi.domain.model.Expense
import tosbik.ao.parayonetimi.ui.theme.AppColor
import tosbik.ao.parayonetimi.utils.Functions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseCard(expense: Expense, onDelete: () -> Unit, onEdit: () -> Unit) {
    val checked = remember { mutableStateOf(false) }
    val expandedMenu = remember { mutableStateOf(false) }
    val deleteProductDialog = remember { mutableStateOf(false) }
    val menuItems = listOf(
        (stringResource(R.string.edit) to AppColor.TitleBlack),
        (stringResource(R.string.delete) to AppColor.ExpenseRed)
    )

    if (deleteProductDialog.value) {
        AlertDialog(
            onDismissRequest = { deleteProductDialog.value = false },
            containerColor = AppColor.DiffBackground,
            shape = RoundedCornerShape(4.dp),
            title = {
                AppText(
                    text = stringResource(R.string.delete_record),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColor.DarkGrey
                )
            },
            text = {
                AppText(
                    text = stringResource(R.string.delete_record_info),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = AppColor.DarkGrey
                )
            },
            confirmButton = {
                AppTextButton(
                    onClick = {
                        deleteProductDialog.value = false
                        onDelete()
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
                    onClick = { deleteProductDialog.value = false },
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

    Box(modifier = Modifier
        .background(AppColor.DiffBackground)
        .padding(bottom = 3.dp)
        .background(AppColor.Coriander)
        .clickable { checked.value = !checked.value }
        .padding(top = 12.dp, bottom = 12.dp, start = 16.dp, end = 4.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                AppText(
                    expense.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppColor.TitleBlack,
                    lineHeight = 20.sp
                )
                AppText(
                    LocalizationUtils.getDateToStringDMY(date = expense.date).first + ", " + LocalizationUtils.getDateToStringDMY(
                        date = expense.date
                    ).second,
                    fontSize = 14.sp,
                    color = AppColor.TitleBlack,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 16.sp,
                    letterSpacing = 0.1.sp
                )
            }
            Row(verticalAlignment = Alignment.Bottom) {
                if (Functions.splitDouble(expense.amount).first.contains("-").not()) {
                    AppText(
                        "+",
                        color = expense.expenseType.textColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        isMoney = true,
                    )
                }
                AppText(
                    Functions.splitDouble(expense.amount).first,
                    color = expense.expenseType.textColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    isMoney = true,
                )
                AppText(
                    ",${
                        Functions.splitDouble(expense.amount).second
                    } ${expense.currency.symbol}",
                    color = expense.expenseType.textColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    isMoney = true, lineHeight = 24.sp,
                )
            }
            Box {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    state = rememberTooltipState(),
                    tooltip = {
                        PlainTooltip { AppText(stringResource(R.string.other_options)) }
                    },
                ) {
                    IconButton(onClick = {
                        expandedMenu.value = true
                    }, modifier = Modifier.size(24.dp)) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = AppColor.TitleBlack,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                DropdownMenu(
                    expanded = expandedMenu.value,
                    onDismissRequest = { expandedMenu.value = false },
                    containerColor = AppColor.Coriander,
                    shadowElevation = 4.dp,
                    shape = RoundedCornerShape(4.dp),
                ) {
                    menuItems.forEachIndexed { index, item ->
                        DropdownMenuItem(text = {
                            AppText(
                                text = item.first,
                                fontSize = 16.sp,
                                color = item.second,
                                fontWeight = FontWeight.Normal,
                                lineHeight = 20.sp
                            )
                        }, onClick = {
                            expandedMenu.value = false
                            if (index == 0) {
                                onEdit()
                            } else {
                                deleteProductDialog.value = true
                            }
                        })
                    }
                }
            }
        }
    }
}