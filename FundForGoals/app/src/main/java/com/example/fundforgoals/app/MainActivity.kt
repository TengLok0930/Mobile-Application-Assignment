package com.example.fundforgoals.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.fundforgoals.core.ui.theme.FundForGoalsTheme
import com.example.fundforgoals.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FundForGoalsTheme {
                AppNavHost()
            }
        }
    }
}