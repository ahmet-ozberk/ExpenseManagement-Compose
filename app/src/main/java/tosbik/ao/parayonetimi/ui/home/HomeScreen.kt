package tosbik.ao.parayonetimi.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import tosbik.ao.parayonetimi.R
import tosbik.ao.parayonetimi.common.CurrencyType
import tosbik.ao.parayonetimi.common.LocalizationUtils
import tosbik.ao.parayonetimi.data.model.AppState
import tosbik.ao.parayonetimi.domain.model.Expense
import tosbik.ao.parayonetimi.ui.components.AppText
import tosbik.ao.parayonetimi.ui.components.EmptyScreen
import tosbik.ao.parayonetimi.ui.components.ErrorDialog
import tosbik.ao.parayonetimi.ui.components.ExpenseCard
import tosbik.ao.parayonetimi.ui.components.LoadingDialog
import tosbik.ao.parayonetimi.ui.components.animateHorizontalAlignmentAsState
import tosbik.ao.parayonetimi.ui.newexpense.ExpenseViewModel
import tosbik.ao.parayonetimi.ui.settings.SettingsScreen
import tosbik.ao.parayonetimi.ui.theme.AppColor
import tosbik.ao.parayonetimi.utils.Functions
import java.time.LocalDate
import java.time.format.TextStyle

sealed class ExpenseType(
    @StringRes val title: Int, val color: Color, @DrawableRes val icon: Int,
    val textColor: Color
) {
    data object Income :
        ExpenseType(
            R.string.income,
            AppColor.IncomeHard,
            R.drawable.arrow_down_circle,
            AppColor.IncomeHard
        )

    data object Expense :
        ExpenseType(
            R.string.expense,
            AppColor.ExpenseHard,
            R.drawable.arrow_up_circle,
            AppColor.HardRed
        )
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController,
    expenseViewModel: ExpenseViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    val totalAmountState by viewModel.totalAmountState.collectAsState()
    val expensesState by viewModel.expensesState.collectAsState()
    val currentDateState by viewModel.currentDateState.collectAsState()
    val settingsBottomSheet = remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val expenseTypes = listOf(ExpenseType.Income, ExpenseType.Expense)
    val activeExpenseType =
        remember { mutableStateOf<ExpenseType>(ExpenseType.Expense) }
    val horizontalBias by remember { mutableFloatStateOf(1f) }
    val tabAlignment by animateHorizontalAlignmentAsState(horizontalBias)
    val loading by viewModel.loadingState.collectAsState()
    val errorMessage by viewModel.errorState.collectAsState()


    var isScrollingUp by remember { mutableStateOf(false) }
    var isAtEndOfList by remember { mutableStateOf(false) }
    var previousScrollPosition by remember { mutableStateOf(0f) }

    LaunchedEffect(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset) {
        val currentScrollPosition =
            listState.firstVisibleItemIndex * 1000f + listState.firstVisibleItemScrollOffset
        isScrollingUp = currentScrollPosition > previousScrollPosition
        previousScrollPosition = currentScrollPosition
        val totalItemsCount = listState.layoutInfo.totalItemsCount
        val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        isAtEndOfList = lastVisibleItemIndex == totalItemsCount - 1
    }

    LaunchedEffect(Unit) {
        listState.scrollToItem(0)
        isScrollingUp = false
        isAtEndOfList = false
    }

    val paddingValue by animateDpAsState(
        targetValue = if (isScrollingUp || isAtEndOfList) 0.dp else 46.dp,
        animationSpec = tween(durationMillis = 200), label = ""
    )

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

    DisposableEffect(key1 = navController) {
        viewModel.initial()
        onDispose {}
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.DiffBackground),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column {
            HomeCustomTopAppbar(paddingValue, totalAmountState)
            HomeTabbar(
                screenWidth,
                tabAlignment,
                expenseTypes,
                activeExpenseType,
                horizontalBias,
                isScrollingUp,
                isAtEndOfList
            )
            Spacer(modifier = Modifier.height(8.dp))
            HomeExpenseList(
                currentDateState,
                expensesState,
                activeExpenseType,
                listState,
                viewModel,
                expenseViewModel,
                navController
            )
        }
        HomeBottomBar(
            navController,
            currentDateState,
            screenWidth,
            viewModel,
            settingsBottomSheet,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
        if (settingsBottomSheet.value) {
            SettingsBottomSheet(settingsBottomSheet, viewModel)
        }
    }
}

@Composable
private fun HomeCustomTopAppbar(
    paddingValue: Dp,
    totalAmountState: AppState<Double>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = WindowInsets.statusBars
                    .asPaddingValues()
                    .calculateTopPadding() + paddingValue,
                bottom = 8.dp
            )
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            if (totalAmountState is AppState.Success) {
                val totalAmount = (totalAmountState as AppState.Success<Double>).data
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Bottom
                ) {
                    if (Functions.splitDouble(totalAmount)
                            .first
                            .contains("-")
                            .not() && totalAmount > 0
                    ) {
                        AppText(
                            "+",
                            color = AppColor.LunarGreen,
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Normal,
                            isMoney = true,
                            modifier = Modifier.height(58.dp)
                        )
                    }
                    AppText(
                        Functions.splitDouble(totalAmount).first,
                        color = AppColor.LunarGreen,
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Normal,
                        isMoney = true, modifier = Modifier.height(58.dp)
                    )
                    AppText(
                        ",${Functions.splitDouble(totalAmount).second}",
                        color = AppColor.LunarGreen,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Normal,
                        isMoney = true, lineHeight = 40.sp,
                    )
                    AppText(
                        " ${CurrencyType.TL.symbol}",
                        color = AppColor.LunarGreen,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        isMoney = true, lineHeight = 40.sp,
                    )
                }
            }
            AppText(
                buildString {
                    append(stringResource(R.string.overview))
                    append(" (")
                    append(stringResource(R.string.monthly))
                    append(")")
                },
                color = AppColor.HardDarkColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 16.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
private fun HomeTabbar(
    screenWidth: Int,
    tabAlignment: BiasAlignment.Horizontal,
    expenseTypes: List<ExpenseType>,
    activeExpenseType: MutableState<ExpenseType>,
    horizontalBias: Float,
    isScrollingUp: Boolean,
    isAtEndOfList: Boolean
) {
    var horizontalBias1 = horizontalBias
    var isScrollingUp1 = isScrollingUp
    var isAtEndOfList1 = isAtEndOfList
    Box(
        modifier = Modifier
            .padding(start = 20.dp, end = 8.dp)
            .width((screenWidth * 0.4 + 8).dp)
            .height(36.dp)
    ) {
        /// Tab Bar Indicator
        Column(
            horizontalAlignment = tabAlignment,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .width((screenWidth * 0.2).dp)
                    .clip(CircleShape)
                    .background(AppColor.LunarGreen)
            )
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            expenseTypes.forEach { expenseType ->
                Box(
                    modifier = Modifier
                        .clickable(interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                activeExpenseType.value = expenseType
                                horizontalBias1 = when (activeExpenseType.value) {
                                    ExpenseType.Income -> -1f
                                    ExpenseType.Expense -> 1f
                                }
                                isScrollingUp1 = false
                                isAtEndOfList1 = false
                            })
                        .fillMaxSize()
                        .weight(1f), contentAlignment = Alignment.Center
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

@Composable
private fun HomeExpenseList(
    currentDateState: AppState<LocalDate>,
    expensesState: AppState<List<Expense>>,
    activeExpenseType: MutableState<ExpenseType>,
    listState: LazyListState,
    viewModel: HomeViewModel,
    expenseViewModel: ExpenseViewModel,
    navController: NavHostController
) {
    if (currentDateState is AppState.Success) {
        val currState = currentDateState as AppState.Success
        when (expensesState) {
            is AppState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is AppState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AppText(text = (expensesState as AppState.Error).message)
                }
            }

            is AppState.Success -> {
                val expenses =
                    (expensesState as AppState.Success<List<Expense>>).data.filter {
                        it.expenseType == activeExpenseType.value && LocalizationUtils.isEqualDate(
                            date1 = it.date,
                            date2 = currState.data
                        )
                    }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 124.dp),
                    state = listState
                ) {
                    if (expenses.isEmpty()) {
                        item {
                            EmptyScreen()
                        }
                    } else {
                        items(expenses) { expense ->
                            ExpenseCard(expense = expense, onDelete = {
                                viewModel.deleteExpense(expense)
                            }, onEdit = {
                                expenseViewModel.updateSelectedExpense(expense)
                                navController.navigate("NewExpense")
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SettingsBottomSheet(
    settingsBottomSheet: MutableState<Boolean>,
    viewModel: HomeViewModel
) {
    ModalBottomSheet(
        onDismissRequest = { settingsBottomSheet.value = false },
        containerColor = AppColor.DiffBackground,
        shape = RoundedCornerShape(4.dp),
        contentWindowInsets = { WindowInsets.ime }
    ) {
        SettingsScreen {
            settingsBottomSheet.value = false
            viewModel.initial()
        }
    }
}

@Composable
private fun HomeBottomBar(
    navController: NavHostController,
    currentDateState: AppState<LocalDate>,
    screenWidth: Int,
    viewModel: HomeViewModel,
    settingsBottomSheet: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        IconButton(
            onClick = {
                navController.navigate("NewExpense")
            },
            modifier = Modifier
                .clip(CircleShape)
                .size(46.dp)
                .background(AppColor.WillowGrove)
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Add",
                tint = AppColor.DiffBackgroundLight,
                modifier = Modifier.size(24.dp)
            )
        }
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(AppColor.LunarGreen)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    12.dp,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (currentDateState) {
                    is AppState.Loading -> {
                        AppText(
                            "...",
                            color = AppColor.DiffBackgroundLight,
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .width((screenWidth * 0.6).dp)
                        )
                    }

                    is AppState.Error -> {
                        AppText(
                            (currentDateState as AppState.Error).message,
                            color = AppColor.DiffBackgroundLight,
                        )
                    }

                    is AppState.Success -> {
                        val currentDate = (currentDateState as AppState.Success).data
                        IconButton(
                            onClick = {
                                viewModel.saveCurrentDate(currentDate.minusMonths(1))
                            }, colors = IconButtonDefaults.iconButtonColors().copy(
                                containerColor = AppColor.WillowGrove
                            ), modifier = Modifier.padding(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.arrow_left),
                                contentDescription = "Back",
                                tint = AppColor.DiffBackgroundLight,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        AppText(
                            LocalizationUtils.getDateToStringMY(date = currentDate).second.toString() + ", " + LocalizationUtils.getDateToStringMY(
                                date = currentDate, textStyle = TextStyle.FULL
                            ).first,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = AppColor.DiffBackgroundLight,
                        )
                        IconButton(
                            onClick = {
                                viewModel.saveCurrentDate(currentDate.plusMonths(1))
                            }, colors = IconButtonDefaults.iconButtonColors().copy(
                                containerColor = AppColor.WillowGrove
                            ), modifier = Modifier.padding(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.arrow_right),
                                contentDescription = "Back",
                                tint = AppColor.DiffBackgroundLight,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
        IconButton(
            onClick = { settingsBottomSheet.value = true },
            modifier = Modifier
                .clip(CircleShape)
                .size(46.dp)
                .background(AppColor.WillowGrove)
        ) {
            Icon(
                painter = painterResource(R.drawable.basil_settings_solid),
                contentDescription = "Settings",
                tint = AppColor.DiffBackgroundLight,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<HomeViewModel>()
    HomeScreen(viewModel = viewModel, navController = navController)
}