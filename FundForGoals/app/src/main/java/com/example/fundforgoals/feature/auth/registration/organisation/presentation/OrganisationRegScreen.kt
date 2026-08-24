package com.example.fundforgoals.feature.auth.registration.organisation.presentation

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundforgoals.R

@Composable
fun OrganisationRegScreen(
    uiState: OrganisationRegUiState,
    onAction: (OrganisationRegAction) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = true
) {
    if (isCompact) {
        OrganisationRegCompactScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )
    } else {
        OrganisationRegExpandedScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )
    }
}

@Composable
private fun OrganisationRegCompactScreen(
    uiState: OrganisationRegUiState,
    onAction: (OrganisationRegAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            OrganisationRegContent(
                uiState = uiState,
                onAction = onAction,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
private fun OrganisationRegExpandedScreen(
    uiState: OrganisationRegUiState,
    onAction: (OrganisationRegAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                OrganisationRegContent(
                    uiState = uiState,
                    onAction = onAction,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp, vertical = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun OrganisationRegContent(
    uiState: OrganisationRegUiState,
    onAction: (OrganisationRegAction) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Registering...",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        else -> {
            Column(
                modifier = modifier.fillMaxSize()
            ) {
                IconButton(
                    onClick = { onAction(OrganisationRegAction.OnBackClick) }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back_40px),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Organisation Registration",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Company Name",
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextField(
                    value = uiState.companyName,
                    onValueChange = {
                        onAction(OrganisationRegAction.OnCompanyNameChanged(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Company Name") },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Password",
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = {
                        onAction(OrganisationRegAction.OnPasswordChanged(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Password") },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Confirm Password",
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextField(
                    value = uiState.confirmPassword,
                    onValueChange = {
                        onAction(OrganisationRegAction.OnConfirmPasswordChanged(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Confirm Password") },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Company Profile",
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            onAction(OrganisationRegAction.OnProfileFileSelected("profile.pdf"))
                        },
                        modifier = Modifier.size(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.upload_40px),
                            contentDescription = "Upload company profile"
                        )
                    }

                    if (uiState.profileFileName != null) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = uiState.profileFileName,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (uiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { onAction(OrganisationRegAction.OnRegisterClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = if (uiState.isLoading) "Registering..." else "Register"
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}