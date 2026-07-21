package com.example.fundforgoals.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fundforgoals.R

@Composable
fun AppNavigationRail(
    selectedItem: String,
    onMessagesClick: () -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    showLabels: Boolean = true
) {
    Surface(
        modifier = modifier.fillMaxHeight(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            NavigationRail(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 12.dp),
                containerColor = MaterialTheme.colorScheme.background,
                header = {
                    Column(
                        modifier = Modifier.padding(bottom = 12.dp),
                        verticalArrangement = Arrangement.Top
                    ) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        )
                    }
                }
            ) {

                NavigationRailItem(
                    selected = selectedItem == "home",
                    onClick = onHomeClick,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.home_40px),
                            contentDescription = "Home"
                        )
                    },
                    label = {
                        if (showLabels) {
                            Text(text = "Home")
                        }
                    },
                    alwaysShowLabel = showLabels,
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    )
                )

                NavigationRailItem(
                    selected = selectedItem == "messages",
                    onClick = onMessagesClick,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.chat_40px),
                            contentDescription = "Messages"
                        )
                    },
                    label = {
                        if (showLabels) {
                            Text(text = "Messages")
                        }
                    },
                    alwaysShowLabel = showLabels,
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    )
                )

                NavigationRailItem(
                    selected = selectedItem == "profile",
                    onClick = onProfileClick,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.account_circle_40px),
                            contentDescription = "Profile"
                        )
                    },
                    label = {
                        if (showLabels) {
                            Text(text = "Profile")
                        }
                    },
                    alwaysShowLabel = showLabels,
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    )
                )
            }
        }
    }
}