package com.example.fundforgoals.core.util

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

enum class MemberHomeContentType {
    LIST_ONLY,
    LIST_AND_DETAIL
}

enum class MemberHomeNavigationType {
    BOTTOM_NAVIGATION,
    NAVIGATION_RAIL_ICONS_ONLY,
    NAVIGATION_RAIL_WITH_LABELS
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun rememberMemberHomeContentType(): MemberHomeContentType {
    val activity = LocalActivity.current ?: return MemberHomeContentType.LIST_ONLY
    val windowSizeClass = calculateWindowSizeClass(activity)

    return when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Medium,
        WindowWidthSizeClass.Expanded -> MemberHomeContentType.LIST_AND_DETAIL

        else -> MemberHomeContentType.LIST_ONLY
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun rememberMemberHomeNavigationType(): MemberHomeNavigationType {
    val activity = LocalActivity.current ?: return MemberHomeNavigationType.BOTTOM_NAVIGATION
    val windowSizeClass = calculateWindowSizeClass(activity)

    return when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> MemberHomeNavigationType.NAVIGATION_RAIL_WITH_LABELS
        WindowWidthSizeClass.Medium -> MemberHomeNavigationType.NAVIGATION_RAIL_ICONS_ONLY
        else -> MemberHomeNavigationType.BOTTOM_NAVIGATION
    }
}