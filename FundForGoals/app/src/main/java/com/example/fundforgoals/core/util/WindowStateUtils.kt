package com.example.fundforgoals.core.util

import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

enum class ContentType {
    LIST_ONLY,
    LIST_AND_DETAIL
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun rememberContentType(): ContentType {
    val activity = LocalActivity.current ?: return ContentType.LIST_ONLY
    val windowSizeClass = calculateWindowSizeClass(activity)

    return when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> ContentType.LIST_AND_DETAIL
        else -> ContentType.LIST_ONLY
    }
}