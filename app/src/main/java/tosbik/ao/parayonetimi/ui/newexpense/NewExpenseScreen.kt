package tosbik.ao.parayonetimi.ui.newexpense

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import tosbik.ao.parayonetimi.R
import tosbik.ao.parayonetimi.common.CurrencyType
import tosbik.ao.parayonetimi.common.LocalizationUtils
import tosbik.ao.parayonetimi.common.addThousandSeparator
import tosbik.ao.parayonetimi.domain.model.Expense
import tosbik.ao.parayonetimi.ui.components.AppButton
import tosbik.ao.parayonetimi.ui.components.AppText
import tosbik.ao.parayonetimi.ui.components.AppTextField
import tosbik.ao.parayonetimi.ui.components.CustomCalendarDialog
import tosbik.ao.parayonetimi.ui.components.CustomKeyboard
import tosbik.ao.parayonetimi.ui.components.CustomKeyboardKey
import tosbik.ao.parayonetimi.ui.components.ErrorDialog
import tosbik.ao.parayonetimi.ui.components.LoadingDialog
import tosbik.ao.parayonetimi.ui.components.animateHorizontalAlignmentAsState
import tosbik.ao.parayonetimi.ui.components.animateSizeAsState
import tosbik.ao.parayonetimi.ui.home.ExpenseType
import tosbik.ao.parayonetimi.ui.theme.AppColor
import java.time.LocalDate

private enum class PriceState {
    Integer, Decimal
}

enum class RepeatTime(@StringRes val title: Int) {
    OneShot(R.string.repeat_oneshot), Monthly(R.string.repeat_monthly)
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewExpenseScreen(
    viewModel: NewExpenseViewModel = hiltViewModel(),
    navController: NavHostController,
    expenseViewModel: ExpenseViewModel = hiltViewModel()
) {
    val expensesState by viewModel.isAddExpenseComplete.collectAsState()
    val showDatePicker = remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val formattedSelectedDate = remember {
        mutableStateOf(
            LocalizationUtils.getDateToStringDMY(selectedDate.value)
        )
    }
    val priceValue = remember { mutableStateOf("00") }
    val priceDecValue = remember { mutableStateOf("00") }
    val productNameValue = remember { mutableStateOf("") }
    val expenseTypes = listOf(ExpenseType.Income, ExpenseType.Expense)
    val activePriceState = remember { mutableStateOf(PriceState.Integer) }
    val activeExpenseType = remember { mutableStateOf<ExpenseType>(ExpenseType.Expense) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val imeVisible = WindowInsets.isImeVisible
    val isKeyboardOpen = remember { mutableStateOf(false) }
    var horizontalBias by remember { mutableFloatStateOf(1f) }
    val tabAlignment by animateHorizontalAlignmentAsState(horizontalBias)

    fun addExpense() {
        val price = "${priceValue.value}.${priceDecValue.value}".toDouble()
        val expense = Expense(
            id = expenseViewModel.selectedExpense?.id ?: 0,
            name = productNameValue.value,
            amount = if (activeExpenseType.value == ExpenseType.Expense) -price else price,
            date = selectedDate.value,
            expenseType = activeExpenseType.value,
            repeat = RepeatTime.OneShot,
            currency = CurrencyType.TL
        )
        if (expenseViewModel.selectedExpense != null) {
            viewModel.updateExpense(expense)
        } else {
            viewModel.addExpense(expense)
        }
    }

    val loading by viewModel.loadingState.collectAsState()
    val errorMessage by viewModel.errorState.collectAsState()

    if (loading) {
        LoadingDialog {
            viewModel.hideLoading()
        }
    }

    errorMessage?.let {
        ErrorDialog(errorMessage = it) {
            viewModel.clearError()
        }
    }

    LaunchedEffect(imeVisible) {
        isKeyboardOpen.value = imeVisible
    }

    LaunchedEffect(expensesState) {
        if (expensesState) {
            navController.popBackStack()
        }
    }

    LaunchedEffect(Unit) {
        expenseViewModel.selectedExpense?.let { editExpense ->
            productNameValue.value = editExpense.name
            selectedDate.value = editExpense.date
            formattedSelectedDate.value = LocalizationUtils.getDateToStringDMY(selectedDate.value)
            val price = editExpense.amount.toString().replace("-", "").split(".")
            priceValue.value = price[0]
            priceDecValue.value = price[1]
            activeExpenseType.value = editExpense.expenseType
            horizontalBias = when (editExpense.expenseType) {
                ExpenseType.Income -> -1f
                ExpenseType.Expense -> 1f
            }
        }
    }

    DisposableEffect(navController) {
        onDispose {
            expenseViewModel.clearSelectedExpense()
        }
    }

    if (showDatePicker.value) {
        CustomCalendarDialog(setShowDialog = {
            showDatePicker.value = it
        }, onSelectedDate = {
            selectedDate.value = it
            formattedSelectedDate.value = LocalizationUtils.getDateToStringDMY(selectedDate.value)
        })
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.DiffBackground)
    ) {
        Spacer(
            modifier = Modifier.height(
                WindowInsets.safeContent.asPaddingValues().calculateTopPadding() + 8.dp,
            )
        )

        /// Tab Bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(16.dp))
            Box(modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable { navController.popBackStack() }
                .padding(8.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Exit",
                    tint = AppColor.Black,
                    modifier = Modifier.size(28.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 8.dp)
            ) {
                /// Tab Bar Indicator
                Column(
                    horizontalAlignment = tabAlignment,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width((screenWidth * 0.5).dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(AppColor.TabBackgroundColor)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    expenseTypes.forEach { expenseType ->
                        Box(
                            modifier = Modifier
                                .clickable(interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        activeExpenseType.value = expenseType
                                        horizontalBias = when (activeExpenseType.value) {
                                            ExpenseType.Income -> -1f
                                            ExpenseType.Expense -> 1f
                                        }
                                    })
                                .width(
                                    animateSizeAsState(
                                        targetWidth = when (expenseType == activeExpenseType.value) {
                                            true -> screenWidth * 0.5f
                                            false -> screenWidth * 0.3f
                                        }
                                    ).value.dp
                                )
                                .fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(expenseType.icon),
                                    contentDescription = "Icon",
                                    tint = if (activeExpenseType.value == expenseType) AppColor.DarkGrey else AppColor.DarkGrey.copy(
                                        alpha = 0.6f
                                    ),
                                    modifier = Modifier.size(18.dp),
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                AppText(
                                    text = stringResource(expenseType.title),
                                    color = if (activeExpenseType.value == expenseType) AppColor.DarkGrey else AppColor.DarkGrey.copy(
                                        alpha = 0.6f
                                    ),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 20.sp,
                                    modifier = Modifier,
                                )
                            }
                        }
                    }
                }
            }
        }
        /// Price and Product Name
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .wrapContentSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                AppText(
                    "${CurrencyType.TL.symbol} ",
                    color = AppColor.DarkGrey,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    isMoney = true, lineHeight = 44.sp,
                    maxLines = 1,
                )
                AppText(text = priceValue.value.addThousandSeparator(),
                    color = AppColor.DarkGrey,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    isMoney = true,
                    maxLines = 1,
                    modifier = Modifier
                        .clickable {
                            activePriceState.value = PriceState.Integer
                        }
                        .background(if (activePriceState.value == PriceState.Integer) AppColor.DiffBackgroundLight else AppColor.Transparent)
                        .padding(horizontal = 4.dp))

                AppText(",${priceDecValue.value}",
                    color = AppColor.DarkGrey,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Normal,
                    isMoney = true,
                    lineHeight = 44.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .clickable {
                            activePriceState.value = PriceState.Decimal
                        }
                        .background(if (activePriceState.value == PriceState.Decimal) AppColor.DiffBackgroundLight else AppColor.Transparent)
                        .padding(horizontal = 4.dp))
            }
            Spacer(modifier = Modifier.height(36.dp))
            Box(modifier = Modifier.padding(horizontal = 36.dp)) {
                AppTextField(
                    value = productNameValue,
                    onValueChange = { productNameValue.value = it },
                    hintText = stringResource(R.string.add_product_name_hint),
                    leading = {
                        Icon(
                            painter = painterResource(id = R.drawable.text_outline),
                            contentDescription = "Product",
                            tint = AppColor.DarkGrey,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            AppButton(
                onClick = { showDatePicker.value = true },
                bgColor = AppColor.Coriander,
                contentColor = AppColor.DarkGrey,
                border = BorderStroke(0.2.dp, AppColor.DarkGrey),
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_outline),
                        contentDescription = "Calendar",
                        tint = AppColor.DarkGrey,
                        modifier = Modifier.size(18.dp)
                    )
                    AppText(text = "${formattedSelectedDate.value.first}, ${formattedSelectedDate.value.second}")
                }
            }
        }
        /// Keyboard
        CustomKeyboard(onKeyPress = {
            keyPressEvent(it)({ number ->
                when (activePriceState.value) {
                    PriceState.Integer -> {
                        if (priceValue.value == "00") {
                            priceValue.value = number.toString()
                        } else {
                            if (priceValue.value.length < 8) {
                                priceValue.value += number.toString()
                            }
                        }
                    }

                    PriceState.Decimal -> {
                        if (priceDecValue.value == "00") {
                            priceDecValue.value = number.toString()
                        } else {
                            if (priceDecValue.value.length < 2) {
                                priceDecValue.value += number.toString()
                            }
                        }
                    }
                }
            }, {
                addExpense()
            }, {
                when (activePriceState.value) {
                    PriceState.Integer -> {
                        if (priceValue.value.length > 1 && priceValue.value != "00") {
                            priceValue.value = priceValue.value.dropLast(1)
                        } else {
                            priceValue.value = "00"
                        }
                    }

                    PriceState.Decimal -> {
                        if (priceDecValue.value.length > 1) {
                            priceDecValue.value = priceDecValue.value.dropLast(1)
                        } else {
                            priceDecValue.value = "00"
                        }
                    }
                }
            })
        })
        Spacer(
            modifier = Modifier.height(32.dp)
        )
    }
}

private fun keyPressEvent(
    key: CustomKeyboardKey,
): (onNumber: ((Int) -> Unit)?, onAdd: (() -> Unit)?, onDelete: (() -> Unit)?) -> Unit =
    { number, onAdd, onDelete ->
        when (key) {
            CustomKeyboardKey.DELETE -> {
                onDelete?.invoke()
            }

            CustomKeyboardKey.ADD -> {
                onAdd?.invoke()
            }

            else -> {
                key.label.toInt().let { num ->
                    number?.invoke(num)
                }
            }
        }
    }


@Preview(showBackground = true, device = Devices.PIXEL_6, showSystemUi = true)
@Composable
fun NewExpenseScreenPreview() {
    val viewModel = hiltViewModel<NewExpenseViewModel>()
    NewExpenseScreen(viewModel = viewModel, navController = rememberNavController())
}