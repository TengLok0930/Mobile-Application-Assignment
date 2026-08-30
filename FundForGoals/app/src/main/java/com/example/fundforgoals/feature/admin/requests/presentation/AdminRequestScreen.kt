package com.example.fundforgoals.feature.admin.requests.presentation

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AdminRequestScreen(
    uiState: AdminRequestUiState,
    onAction: (AdminRequestAction) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = true
) {
    if (isCompact) {
        AdminRequestCompactScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )
    } else {
        AdminRequestExpandedScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )
    }
}

@Composable
private fun AdminRequestCompactScreen(
    uiState: AdminRequestUiState,
    onAction: (AdminRequestAction) -> Unit,
    modifier: Modifier = Modifier
) {
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
}

@Composable
private fun AdminRequestCompactIncomingScreen(
    uiState: AdminRequestUiState,
    onAction: (AdminRequestAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AdminBottomBar(
                selectedItem = "requests",
                onRequestsClick = { onAction(AdminRequestAction.OnRequestsClick) },
                onHomeClick = { onAction(AdminRequestAction.OnHomeClick) },
                onProfileClick = { onAction(AdminRequestAction.OnProfileClick) }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Incoming requests",
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
            }
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

    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AdminBottomBar(
                selectedItem = "requests",
                onRequestsClick = { onAction(AdminRequestAction.OnRequestsClick) },
                onHomeClick = { onAction(AdminRequestAction.OnHomeClick) },
                onProfileClick = { onAction(AdminRequestAction.OnProfileClick) }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            CategoryRequestsPane(
                title = title,
                requests = uiState.requestItems,
                showBack = true,
                onBackClick = { onAction(AdminRequestAction.OnBackFromCategoryClick) },
                onRequestClick = { requestId ->
                    onAction(AdminRequestAction.OnRequestClick(requestId))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
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
    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AdminBottomBar(
                selectedItem = "requests",
                onRequestsClick = { onAction(AdminRequestAction.OnRequestsClick) },
                onHomeClick = { onAction(AdminRequestAction.OnHomeClick) },
                onProfileClick = { onAction(AdminRequestAction.OnProfileClick) }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            RequestDetailPane(
                request = uiState.selectedRequest,
                showBack = true,
                isLoading = uiState.isLoading,
                onBackClick = { onAction(AdminRequestAction.OnBackFromDetailClick) },
                onGenerateAiOverviewClick = { onAction(AdminRequestAction.OnGenerateAiOverviewClick) },
                onAcceptClick = { onAction(AdminRequestAction.OnAcceptRequestClick) },
                onRejectClick = { onAction(AdminRequestAction.OnRejectRequestClick) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
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
                .padding(20.dp)
        ) {
            AdminNavigationRail(
                selectedItem = "requests",
                onRequestsClick = { onAction(AdminRequestAction.OnRequestsClick) },
                onHomeClick = { onAction(AdminRequestAction.OnHomeClick) },
                onProfileClick = { onAction(AdminRequestAction.OnProfileClick) }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Loading requests...",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 16.sp
                            )
                        }
                    }

                    uiState.errorMessage != null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = uiState.errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 16.sp
                            )
                        }
                    }

                    else -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Incoming requests",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            IncomingCategoryList(
                                categories = uiState.categories,
                                onCategoryClick = { type ->
                                    onAction(AdminRequestAction.OnCategoryClick(type))
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                when {
                    uiState.selectedType == null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Select a category",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 18.sp
                            )
                        }
                    }

                    uiState.selectedRequest == null -> {
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

                    else -> {
                        RequestDetailPane(
                            request = uiState.selectedRequest,
                            showBack = true,
                            isLoading = uiState.isLoading,
                            onBackClick = {
                                onAction(AdminRequestAction.OnBackFromDetailClick)
                            },
                            onGenerateAiOverviewClick = {
                                onAction(AdminRequestAction.OnGenerateAiOverviewClick)
                            },
                            onAcceptClick = {
                                onAction(AdminRequestAction.OnAcceptRequestClick)
                            },
                            onRejectClick = {
                                onAction(AdminRequestAction.OnRejectRequestClick)
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
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
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(categories, key = { it.type.name }) { category ->
            CategoryCard(
                category = category,
                onClick = { onCategoryClick(category.type) }
            )
        }
    }
}

@Composable
private fun CategoryCard(
    category: AdminRequestCategoryUi,
    onClick: () -> Unit
) {
    val accentColor = if (isSystemInDarkTheme()) {
        BrandAccentDark
    } else {
        BrandAccentLight
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = category.title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (category.count > 0) {
                    "Incoming (${category.count})"
                } else {
                    "No Requests"
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = category.buttonText,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
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
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(requests, key = { it.id }) { request ->
                    RequestCard(
                        request = request,
                        onClick = { onRequestClick(request.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RequestCard(
    request: AdminRequestItemUi,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(56.dp)
                    .width(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = request.title.firstOrNull()?.uppercase() ?: "R",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = request.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                request.subtitle?.let {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = it,
                        color = BrandAccentLight,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun RequestDetailPane(
    request: AdminRequestItemUi?,
    showBack: Boolean,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onGenerateAiOverviewClick: () -> Unit,
    onAcceptClick: () -> Unit,
    onRejectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (request == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Select a request to view details.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 18.sp
            )
        }
        return
    }

    val accentColor = if (isSystemInDarkTheme()) BrandAccentDark else BrandAccentLight
    val formattedStatus = request.status.replaceFirstChar { it.uppercase() }
    val hasGeneratedOverview = request.hasAiOverview

    Column(
        modifier = modifier
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (showBack) {
            BackButton(onClick = onBackClick)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Text(
            text = request.title,
            color = accentColor,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Status: $formattedStatus",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Created at: ${request.createdAt.toDisplayDateTime()}",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Details",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = request.details,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "AI summary",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = request.aiSummary,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onGenerateAiOverviewClick,
            enabled = !hasGeneratedOverview && !isLoading,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            if (isLoading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generating overview...")
                }
            } else {
                Text(
                    text = if (hasGeneratedOverview) {
                        "AI Overview Generated"
                    } else {
                        "Generate AI Overview"
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onAcceptClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Approve")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onRejectClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text("Reject")
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

private fun String.toDisplayDateTime(): String {
    return try {
        val normalized = replace(" ", "T").let {
            if (it.endsWith("+00")) it + ":00" else it
        }

        val malaysiaZone = ZoneId.of("Asia/Kuala_Lumpur")
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

        OffsetDateTime
            .parse(normalized)
            .atZoneSameInstant(malaysiaZone)
            .format(formatter)
    } catch (e: Exception) {
        this
    }
}