package com.example.fundforgoals.feature.auth.registration.signup_choice.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundforgoals.R

@Composable
fun SignUpChoiceScreen(
    uiState: SignUpChoiceUiState,
    onAction: (SignUpChoiceAction) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = true
) {
    if (isCompact) {
        SignUpChoiceCompactScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )
    } else {
        SignUpChoiceExpandedScreen(
            uiState = uiState,
            onAction = onAction,
            modifier = modifier
        )
    }
}

@Composable
private fun SignUpChoiceCompactScreen(
    uiState: SignUpChoiceUiState,
    onAction: (SignUpChoiceAction) -> Unit,
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
            SignUpChoiceContent(
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
private fun SignUpChoiceExpandedScreen(
    uiState: SignUpChoiceUiState,
    onAction: (SignUpChoiceAction) -> Unit,
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
                    .fillMaxHeight(0.85f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                SignUpChoiceContent(
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
private fun SignUpChoiceContent(
    uiState: SignUpChoiceUiState,
    onAction: (SignUpChoiceAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            IconButton(
                onClick = { onAction(SignUpChoiceAction.OnBackClick) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back_40px),
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "What would you sign up as ?",
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 34.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 42.sp
            )

            Spacer(modifier = Modifier.height(72.dp))

            Button(
                onClick = { onAction(SignUpChoiceAction.OnMemberClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Member",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { onAction(SignUpChoiceAction.OnOrganisationClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Organisation",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}