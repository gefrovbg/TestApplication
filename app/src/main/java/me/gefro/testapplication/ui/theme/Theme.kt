package me.gefro.testapplication.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFB07B57),
    secondary = Color(0xFFD2A679),
    tertiary = Color(0xFFF4E1C8),
    background = Color(0xFFFFF5E7),
    surface = Color(0xFFFFF2DF),
    onPrimary = Color.White,
    onSecondary = Color(0xFF4B2F1B),
    onTertiary = Color(0xFF4B2F1B),
    onBackground = Color(0xFF3E2723),
    onSurface = Color(0xFF3E2723)
)


private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4E342E),
    secondary = Color(0xFF6D4C41),
    tertiary = Color(0xFF8D6E63),
    background = Color(0xFF3E2723),
    surface = Color(0xFF4E342E),
    onPrimary = Color(0xFFFFE0B2),
    onSecondary = Color(0xFFFFE0B2),
    onTertiary = Color(0xFFFFE0B2),
    onBackground = Color(0xFFFFE0B2),
    onSurface = Color(0xFFFFE0B2)
)

val LocalThemeResources = staticCompositionLocalOf {
    lightColorScheme()
}

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("NavController not provided")
}

@Composable
fun TestApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val navController = rememberNavController()

    CompositionLocalProvider(
        LocalThemeResources provides colorScheme,
        LocalNavController provides navController
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }

}