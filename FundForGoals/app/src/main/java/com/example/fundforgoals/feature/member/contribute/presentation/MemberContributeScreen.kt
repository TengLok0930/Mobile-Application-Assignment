/*
package com.example.fundforgoals.feature.member.contribute.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fundforgoals.app.navigation.AppBottomBar

@Composable
fun MemberContributeScreen(
    uiState: MemberContributeUiState,
    onAction: (MemberContributeAction) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = true
) {
    if (isCompact) {
        MemberContributeCompactScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )
    } else {
        MemberContributeExpandedScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )
    }
}

@Composable
fun MemberContributeCompactScreen(
    uiState: MemberContributeUiState,
    onAction: (MemberContributeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AppBottomBar(
                selectedItem = "home",
                onMessagesClick = { onAction(MemberContributeAction.OnMessagesClick) },
                onHomeClick = { onAction(MemberContributeAction.OnHomeClick) },
                onProfileClick = { onAction(MemberContributeAction.OnProfileClick) }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            MemberContributeContent(
                uiState = uiState,
                onAction = onAction,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun MemberContributeExpandedScreen(
    uiState: MemberContributeUiState,
    onAction: (MemberContributeAction) -> Unit,
    modifier: Modifier = Modifier
) {

}

@Composable
fun MemberContributeContent(
    uiState: MemberContributeUiState,
    onAction: (MemberContributeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme
    ) {

    }
}*/
