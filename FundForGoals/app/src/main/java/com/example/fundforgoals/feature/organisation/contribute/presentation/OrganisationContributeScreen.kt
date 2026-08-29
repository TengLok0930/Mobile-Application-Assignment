package com.example.fundforgoals.feature.organisation.contribute.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fundforgoals.core.ui.components.navigation.BackButton
import com.example.fundforgoals.core.ui.theme.BrandAccentDark
import com.example.fundforgoals.core.ui.theme.BrandAccentLight

@Composable
fun OrganisationContributeScreen(
    uiState: OrganisationContributeUiState,
    onAction: (OrganisationContributeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)

    val accentColor = if (isSystemInDarkTheme()) {
        BrandAccentDark
    } else {
        BrandAccentLight
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        cursorColor = MaterialTheme.colorScheme.primary
    )

    Surface(
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                BackButton(
                    onClick = { onAction(OrganisationContributeAction.OnBackClick) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            when {
                uiState.isLoading -> {
                    Text(
                        text = "Loading...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                uiState.project == null -> {
                    Text(
                        text = uiState.errorMessage ?: "Project not found",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {
                    val project = uiState.project
                    val progress = if (project.fundGoal > 0.0) {
                        (uiState.currentFund / project.fundGoal)
                            .toFloat()
                            .coerceIn(0f, 1f)
                    } else {
                        0f
                    }

                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        if (project.avatarUrl.isNotBlank()) {
                            AsyncImage(
                                model = project.avatarUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        } else {
                            Text(
                                text = project.title.firstOrNull()?.uppercase() ?: "P",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = project.title,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "by ${uiState.creatorName}",
                        color = accentColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "RM %.2f raised of RM %.2f".format(
                            uiState.currentFund,
                            project.fundGoal
                        ),
                        modifier = Modifier.align(Alignment.Start),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        modifier = Modifier.align(Alignment.Start),
                        text = "Make a Contribution",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    LabeledField(
                        label = "Amount (RM)",
                        value = uiState.fundAmountInput,
                        placeholder = "0.00",
                        placeholderColor = placeholderColor,
                        colors = textFieldColors,
                        onValueChange = { onAction(OrganisationContributeAction.OnFundAmountChanged(it)) }
                    )

                    uiState.errorMessage?.let { error ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error,
                            modifier = Modifier.align(Alignment.Start),
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp
                        )
                    }

                    if (uiState.submitSuccess) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Thank you for your contribution!",
                            modifier = Modifier.align(Alignment.Start),
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { onAction(OrganisationContributeAction.OnSubmitClick) },
                        enabled = !uiState.isSubmitting && uiState.fundAmountInput.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (uiState.isSubmitting) "Submitting..." else "Contribute")
                    }
                }
            }
        }
    }
}

@Composable
private fun LabeledField(
    label: String,
    value: String,
    placeholder: String,
    placeholderColor: androidx.compose.ui.graphics.Color,
    colors: androidx.compose.material3.TextFieldColors,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onTogglePasswordVisibility: (() -> Unit)? = null
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = label,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder, color = placeholderColor)
        },
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword && !isPasswordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = if (isPassword && onTogglePasswordVisibility != null) {
            {
                TextButton(onClick = onTogglePasswordVisibility) {
                    Text(
                        text = if (isPasswordVisible) "Hide" else "Show",
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            null
        },
        colors = colors
    )
}