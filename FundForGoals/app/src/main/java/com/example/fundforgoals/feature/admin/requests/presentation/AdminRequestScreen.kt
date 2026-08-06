package com.example.fundforgoals.feature.admin.requests.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundforgoals.app.navigation.AdminBottomBar
import com.example.fundforgoals.app.navigation.AdminNavigationRail
import com.example.fundforgoals.core.ui.components.navigation.BackButton
import com.example.fundforgoals.core.ui.theme.BrandAccentDark
import com.example.fundforgoals.core.ui.theme.BrandAccentLight

@Composable
fun AdminRequestScreen(
    uiState: AdminRequestUiState,
    onAction: (AdminRequestAction) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = true
) {
    if (isCompact) {
        when {
            uiState.selectedType == null -> {
                AdminRequestCompactIncomingScreen(
                    uiState = uiState,
                    onAction = onAction,
                    modifier = modifier
                )
            }

            uiState.selectedRequestId == null -> {
                AdminRequestCompactCategoryScreen(
                    uiState = uiState,
                    onAction = onAction,
                    modifier = modifier
                )
            }

            else -> {
                AdminRequestCompactDetailScreen(
                    uiState = uiState,
                    onAction = onAction,
                    modifier = modifier
                )
            }
        }
    } else {
        AdminRequestExpandedScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )
    }
}

@Composable
private fun AdminRequestCompactIncomingScreen(
    uiState: AdminRequestUiState,
    onAction: (AdminRequestAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Requests",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            IncomingCategoryList(
                categories = uiState.categories,
                onCategoryClick = { type ->
                    onAction(AdminRequestAction.OnCategoryClick(type))
                },
                modifier = Modifier.weight(1f)
            )

            AdminBottomBar(
                selectedItem = "requests",
                onRequestsClick = { onAction(AdminRequestAction.OnRequestsClick) },
                onHomeClick = { onAction(AdminRequestAction.OnHomeClick) },
                onProfileClick = { onAction(AdminRequestAction.OnProfileClick) }
            )
        }
    }
}

@Composable
private fun AdminRequestCompactCategoryScreen(
    uiState: AdminRequestUiState,
    onAction: (AdminRequestAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val title = uiState.selectedType?.toCategoryTitle() ?: "Requests"

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            CategoryRequestsPane(
                title = title,
                requests = uiState.requestItems,
                showBack = true,
                onBackClick = { onAction(AdminRequestAction.OnBackFromCategoryClick) },
                onRequestClick = { requestId ->
                    onAction(AdminRequestAction.OnRequestClick(requestId))
                },
                modifier = Modifier.weight(1f)
            )

            AdminBottomBar(
                selectedItem = "requests",
                onRequestsClick = { onAction(AdminRequestAction.OnRequestsClick) },
                onHomeClick = { onAction(AdminRequestAction.OnHomeClick) },
                onProfileClick = { onAction(AdminRequestAction.OnProfileClick) }
            )
        }
    }
}

@Composable
private fun AdminRequestCompactDetailScreen(
    uiState: AdminRequestUiState,
    onAction: (AdminRequestAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            RequestDetailPane(
                request = uiState.selectedRequest,
                showBack = true,
                onBackClick = { onAction(AdminRequestAction.OnBackFromDetailClick) },
                onAcceptClick = { onAction(AdminRequestAction.OnAcceptRequestClick) },
                onRejectClick = { onAction(AdminRequestAction.OnRejectRequestClick) },
                modifier = Modifier.weight(1f)
            )

            AdminBottomBar(
                selectedItem = "requests",
                onRequestsClick = { onAction(AdminRequestAction.OnRequestsClick) },
                onHomeClick = { onAction(AdminRequestAction.OnHomeClick) },
                onProfileClick = { onAction(AdminRequestAction.OnProfileClick) }
            )
        }
    }
}

@Composable
private fun AdminRequestExpandedScreen(
    uiState: AdminRequestUiState,
    onAction: (AdminRequestAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryTitle = uiState.selectedType?.toCategoryTitle() ?: "Requests"

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            AdminNavigationRail(
                selectedItem = "requests",
                onRequestsClick = { onAction(AdminRequestAction.OnRequestsClick) },
                onHomeClick = { onAction(AdminRequestAction.OnHomeClick) },
                onProfileClick = { onAction(AdminRequestAction.OnProfileClick) }
            )

            Spacer(modifier = Modifier.width(20.dp))

            Card(
                modifier = Modifier
                    .weight(0.95f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Incoming requests",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    IncomingCategoryList(
                        categories = uiState.categories,
                        onCategoryClick = { type ->
                            onAction(AdminRequestAction.OnCategoryClick(type))
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Card(
                modifier = Modifier
                    .weight(1.05f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                CategoryRequestsPane(
                    title = categoryTitle,
                    requests = uiState.requestItems,
                    showBack = false,
                    onBackClick = {},
                    onRequestClick = { requestId ->
                        onAction(AdminRequestAction.OnRequestClick(requestId))
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                RequestDetailPane(
                    request = uiState.selectedRequest,
                    showBack = false,
                    onBackClick = {},
                    onAcceptClick = { onAction(AdminRequestAction.OnAcceptRequestClick) },
                    onRejectClick = { onAction(AdminRequestAction.OnRejectRequestClick) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun IncomingCategoryList(
    categories: List<AdminRequestCategoryUi>,
    onCategoryClick: (AdminRequestType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        items(categories, key = { it.type.name }) { category ->
            CategorySection(
                category = category,
                onClick = { onCategoryClick(category.type) }
            )
        }
    }
}

@Composable
private fun CategorySection(
    category: AdminRequestCategoryUi,
    onClick: () -> Unit
) {
    val buttonColor = if (isSystemInDarkTheme()) {
        BrandAccentDark
    } else {
        BrandAccentLight
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (category.count > 0) {
                "${category.title} (${category.count})"
            } else {
                category.title
            },
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = category.buttonText,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun CategoryRequestsPane(
    title: String,
    requests: List<AdminRequestItemUi>,
    showBack: Boolean,
    onBackClick: () -> Unit,
    onRequestClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBack) {
                BackButton(onClick = onBackClick)
                Spacer(modifier = Modifier.width(12.dp))
            }

            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (requests.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No requests available.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(requests, key = { it.id }) { request ->
                    RequestRow(
                        request = request,
                        onClick = { onRequestClick(request.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RequestRow(
    request: AdminRequestItemUi,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = request.username,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = request.subtitle,
                modifier = Modifier.widthIn(max = 180.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    }
}

@Composable
private fun RequestDetailPane(
    request: AdminRequestItemUi?,
    showBack: Boolean,
    onBackClick: () -> Unit,
    onAcceptClick: () -> Unit,
    onRejectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBack) {
                BackButton(onClick = onBackClick)
                Spacer(modifier = Modifier.width(12.dp))
            }

            Text(
                text = request?.username ?: "Request details",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (request == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Select a request to view details.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp
                )
            }
        } else {
            Text(
                text = "AI summary",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = request.aiSummary,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onAcceptClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Accept Request",
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onRejectClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text(
                        text = "Reject Request",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun AdminRequestType.toCategoryTitle(): String {
    return when (this) {
        AdminRequestType.USER -> "New user requests"
        AdminRequestType.ORGANISATION -> "New organisation requests"
        AdminRequestType.PROJECT -> "New project requests"
    }
}