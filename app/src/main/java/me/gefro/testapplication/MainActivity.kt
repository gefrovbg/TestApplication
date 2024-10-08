package me.gefro.testapplication

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import me.gefro.testapplication.ui.containers.MainContainer
import me.gefro.testapplication.ui.screens.splash.SplashScreenViewModel
import me.gefro.testapplication.ui.theme.TestApplicationTheme
import me.gefro.testapplication.ui.tools.SafeArea
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {

    private val splashScreenViewModel: SplashScreenViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition{
            splashScreenViewModel.isSplashShow.value
        }

        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1001)
        }

        enableEdgeToEdge()
        setContent {

            val topSafeArea = WindowInsets.statusBars.asPaddingValues().calculateTopPadding().value
            val bottomSafeArea = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding().value

            KoinContext {

                val safeArea: SafeArea by inject()

                LaunchedEffect(topSafeArea, bottomSafeArea) {
                    safeArea.setTop(topSafeArea)
                    safeArea.setBottom(bottomSafeArea)
                }

                TestApplicationTheme {
                    MainContainer()
                }
            }
        }
    }
}