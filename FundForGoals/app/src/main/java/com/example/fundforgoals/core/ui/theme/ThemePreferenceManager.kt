package com.example.fundforgoals.core.ui.theme

import android.content.Context

class ThemePreferenceManager(context: Context) {

    private val prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    fun isDarkTheme(): Boolean {
        return prefs.getBoolean(KEY_IS_DARK_THEME, true)
    }

    fun setDarkTheme(isDarkTheme: Boolean) {
        prefs.edit()
            .putBoolean(KEY_IS_DARK_THEME, isDarkTheme)
            .apply()
    }

    companion object {
        private const val KEY_IS_DARK_THEME = "is_dark_theme"
    }
}