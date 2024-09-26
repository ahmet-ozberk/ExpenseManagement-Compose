package tosbik.ao.parayonetimi.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import tosbik.ao.parayonetimi.ui.home.HomeScreen
import tosbik.ao.parayonetimi.ui.newexpense.NewExpenseScreen
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry
import tosbik.ao.parayonetimi.ui.newexpense.ExpenseViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
) {
    val expenseViewModel = hiltViewModel<ExpenseViewModel>()
    NavHost(
        modifier = Modifier.then(modifier),
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(
            "Home",
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() },
            popEnterTransition = { rightToLeftBack() },
            popExitTransition = { leftToRightBack() }) {
            HomeScreen(navController = navController, expenseViewModel = expenseViewModel)
        }
        composable(
            "NewExpense",
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() },
            popEnterTransition = { rightToLeftBack() },
            popExitTransition = { leftToRightBack() }) {
            NewExpenseScreen(navController = navController, expenseViewModel = expenseViewModel)
        }
    }
}


fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition() = slideIntoContainer(
    AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)
) + fadeIn(animationSpec = tween(300))

fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition() = slideOutOfContainer(
    AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)
) + fadeOut(animationSpec = tween(300))

fun AnimatedContentTransitionScope<NavBackStackEntry>.rightToLeftBack() =
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)
    ) + fadeIn(animationSpec = tween(300))

fun AnimatedContentTransitionScope<NavBackStackEntry>.leftToRightBack() =
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)
    ) + fadeOut(animationSpec = tween(300))