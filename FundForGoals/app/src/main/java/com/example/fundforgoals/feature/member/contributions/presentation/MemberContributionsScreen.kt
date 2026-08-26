package com.example.fundforgoals.feature.member.contributions.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.fundforgoals.R
import com.example.fundforgoals.app.navigation.AppBottomBar
import com.example.fundforgoals.app.navigation.AppNavigationRail

@Composable
fun MemberContributionsScreen(
    uiState: MemberContributionsUiState,
    onAction: (MemberContributionsAction) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = true
) {
    var selectedCertificate by remember { mutableStateOf<MemberContributionUi?>(null) }

    if (isCompact) {
        MemberContributionsCompactScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier,
            selectedCertificate = selectedCertificate,
            onSelectCertificate = { selectedCertificate = it },
            onDismissCertificate = { selectedCertificate = null }
        )
    } else {
        MemberContributionsExpandedScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier,
            selectedCertificate = selectedCertificate,
            onSelectCertificate = { selectedCertificate = it },
            onDismissCertificate = { selectedCertificate = null }
        )
    }
}

@Composable
private fun MemberContributionsCompactScreen(
    uiState: MemberContributionsUiState,
    onAction: (MemberContributionsAction) -> Unit,
    modifier: Modifier = Modifier,
    selectedCertificate: MemberContributionUi?,
    onSelectCertificate: (MemberContributionUi?) -> Unit,
    onDismissCertificate: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .then(
                    if (selectedCertificate != null) Modifier.blur(10.dp) else Modifier
                ),
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                AppBottomBar(
                    selectedItem = "profile",
                    onMessagesClick = { onAction(MemberContributionsAction.OnMessagesClick) },
                    onHomeClick = { onAction(MemberContributionsAction.OnHomeClick) },
                    onProfileClick = { onAction(MemberContributionsAction.OnProfileClick) }
                )
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                MemberContributionsContent(
                    uiState = uiState,
                    onAction = onAction,
                    modifier = Modifier.fillMaxSize(),
                    onSelectCertificate = onSelectCertificate
                )
            }
        }

        selectedCertificate?.let { contribution ->
            CertificateDialog(
                memberName = uiState.memberName,
                contribution = contribution,
                onDismiss = onDismissCertificate
            )
        }
    }
}

@Composable
private fun MemberContributionsExpandedScreen(
    uiState: MemberContributionsUiState,
    onAction: (MemberContributionsAction) -> Unit,
    modifier: Modifier = Modifier,
    selectedCertificate: MemberContributionUi?,
    onSelectCertificate: (MemberContributionUi?) -> Unit,
    onDismissCertificate: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (selectedCertificate != null) Modifier.blur(10.dp) else Modifier
                ),
            color = MaterialTheme.colorScheme.background
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(20.dp)
            ) {
                AppNavigationRail(
                    selectedItem = "profile",
                    onMessagesClick = { onAction(MemberContributionsAction.OnMessagesClick) },
                    onHomeClick = { onAction(MemberContributionsAction.OnHomeClick) },
                    onProfileClick = { onAction(MemberContributionsAction.OnProfileClick) }
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
                    MemberContributionsContent(
                        uiState = uiState,
                        onAction = onAction,
                        modifier = Modifier.fillMaxSize(),
                        onSelectCertificate = onSelectCertificate
                    )
                }
            }
        }

        selectedCertificate?.let { contribution ->
            CertificateDialog(
                memberName = uiState.memberName,
                contribution = contribution,
                onDismiss = onDismissCertificate
            )
        }
    }
}

@Composable
private fun MemberContributionsContent(
    uiState: MemberContributionsUiState,
    onAction: (MemberContributionsAction) -> Unit,
    modifier: Modifier = Modifier,
    onSelectCertificate: (MemberContributionUi?) -> Unit
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading...",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        uiState.errorMessage != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        else -> {
            Column(
                modifier = modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onAction(MemberContributionsAction.OnBackClick) }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back_40px),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Contributions",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "Ongoing",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    items(uiState.ongoingContributions) { contribution ->
                        ContributionCard(
                            contribution = contribution,
                            onClick = {
                                onAction(
                                    MemberContributionsAction.OnContributionClick(
                                        contribution.id
                                    )
                                )
                            },
                            onECertClick = { }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Past",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    items(uiState.pastContributions) { contribution ->
                        ContributionCard(
                            contribution = contribution,
                            onClick = {
                                onAction(
                                    MemberContributionsAction.OnContributionClick(
                                        contribution.id
                                    )
                                )
                            },
                            onECertClick = {
                                onSelectCertificate(contribution)
                                onAction(
                                    MemberContributionsAction.OnECertClick(
                                        contribution.id
                                    )
                                )
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ContributionCard(
    contribution: MemberContributionUi,
    onClick: () -> Unit,
    onECertClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contribution.projectTitle.take(1),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = contribution.projectTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = contribution.organisationName,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (contribution.isOngoing) {
                    Text(
                        text = "Contributed ${contribution.amountText}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                } else if (contribution.hasECertificate) {
                    TextButton(
                        onClick = onECertClick
                    ) {
                        Text(
                            text = "View e-cert",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CertificateDialog(
    memberName: String,
    contribution: MemberContributionUi,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.person_40px),
                        contentDescription = "Certificate",
                        modifier = Modifier.size(44.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Certificate of Contribution",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Thank you $memberName for contributing ${contribution.amountText} to ${contribution.projectTitle}",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Close",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}