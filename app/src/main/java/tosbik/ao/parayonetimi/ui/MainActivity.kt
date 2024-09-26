package tosbik.ao.parayonetimi.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tosbik.ao.parayonetimi.R
import tosbik.ao.parayonetimi.common.CurrencyType
import tosbik.ao.parayonetimi.data.source.local.ExpenseDao
import tosbik.ao.parayonetimi.domain.model.Expense
import tosbik.ao.parayonetimi.ui.home.ExpenseType
import tosbik.ao.parayonetimi.ui.navigation.NavigationGraph
import tosbik.ao.parayonetimi.ui.newexpense.RepeatTime
import tosbik.ao.parayonetimi.ui.theme.AppColor
import tosbik.ao.parayonetimi.ui.theme.MyappTheme
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dao: ExpenseDao

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyappTheme(darkTheme = false) {
                Surface(modifier = Modifier.fillMaxSize(), contentColor = Color.White) {
                    val navController = rememberNavController()
                    val startDestination = "Home"
                    NavigationGraph(
                        navController = navController,
                        startDestination = startDestination,
                    )
                }
            }
        }
    }
}