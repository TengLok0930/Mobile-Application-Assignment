package com.example.fundforgoals.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fundforgoals.R

@Composable
fun AppNavigationRail(
    modifier: Modifier = Modifier,
    selectedItem: String,
    onMessagesClick: () -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    showLabels: Boolean = false
) {
    NavigationRail(
        modifier = modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NavigationRailItem(
                selected = selectedItem == "messages",
                onClick = onMessagesClick,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.chat_40px),
                        contentDescription = "Messages",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = if (showLabels) {
                    { Text("Messages") }
                } else null,
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                )
            )

            NavigationRailItem(
                selected = selectedItem == "home",
                onClick = onHomeClick,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.home_40px),
                        contentDescription = "Home",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = if (showLabels) {
                    { Text("Home") }
                } else null,
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                )
            )

            NavigationRailItem(
                selected = selectedItem == "profile",
                onClick = onProfileClick,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.account_circle_40px),
                        contentDescription = "Profile",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = if (showLabels) {
                    { Text("Profile") }
                } else null,
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                )
            )
        }
    }
}

@Composable
fun AdminNavigationRail(
    selectedItem: String,
    onRequestsClick: () -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    showLabels: Boolean = false,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NavigationRailItem(
                selected = selectedItem == "requests",
                onClick = onRequestsClick,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.group_search_40px),
                        contentDescription = "Requests",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = if (showLabels) {
                    { Text("Requests") }
                } else null,
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                )
            )

            NavigationRailItem(
                selected = selectedItem == "home",
                onClick = onHomeClick,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.home_40px),
                        contentDescription = "Home",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = if (showLabels) {
                    { Text("Home") }
                } else null,
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                )
            )

            NavigationRailItem(
                selected = selectedItem == "profile",
                onClick = onProfileClick,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.account_circle_40px),
                        contentDescription = "Profile",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = if (showLabels) {
                    { Text("Profile") }
                } else null,
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                )
            )
        }
    }
}