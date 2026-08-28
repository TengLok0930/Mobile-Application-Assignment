package com.example.fundforgoals.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.fundforgoals.core.ui.theme.FundForGoalsTheme
import com.example.fundforgoals.core.ui.theme.ThemePreferenceManager
import com.example.fundforgoals.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themePreferenceManager = ThemePreferenceManager(this)

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)

            var isDarkTheme by remember {
                mutableStateOf(themePreferenceManager.isDarkTheme())
            }

            FundForGoalsTheme(
                darkTheme = isDarkTheme
            ) {
                AppNavHost(
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = {
                        val newTheme = !isDarkTheme
                        isDarkTheme = newTheme
                        themePreferenceManager.setDarkTheme(newTheme)
                    }
                )
            }
        }
    }
}