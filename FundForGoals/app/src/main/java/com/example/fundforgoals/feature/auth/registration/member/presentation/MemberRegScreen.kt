package com.example.fundforgoals.feature.auth.registration.member

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundforgoals.R
import com.example.fundforgoals.core.ui.components.navigation.BackButton
import com.example.fundforgoals.core.ui.theme.BrandAccentDark
import com.example.fundforgoals.core.ui.theme.BrandAccentLight
import com.example.fundforgoals.core.ui.theme.FundForGoalsTheme
import com.example.fundforgoals.feature.auth.forgotpassword.presentation.ForgotPasswordAction
import com.example.fundforgoals.feature.auth.registration.member.presentation.MemberRegAction
import com.example.fundforgoals.feature.auth.registration.organisation.presentation.OrganisationRegAction

@Composable
fun MemberRegScreen(
    uiState: MemberRegUiState,
    onAction: (MemberRegAction) -> Unit,
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
                    onClick = { onAction(MemberRegAction.OnBackClick) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                modifier = Modifier.align(Alignment.Start),
                text = "Create Member Account",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            LabeledField(
                label = "Username",
                value = uiState.username,
                placeholder = "Choose a username",
                placeholderColor = placeholderColor,
                colors = textFieldColors,
                onValueChange = { onAction(MemberRegAction.OnUsernameChanged(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LabeledField(
                label = "Social URL",
                value = uiState.socialUrl,
                placeholder = "Link to your social profile",
                placeholderColor = placeholderColor,
                colors = textFieldColors,
                keyboardType = KeyboardType.Uri,
                onValueChange = { onAction(MemberRegAction.OnSocialUrlChanged(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LabeledField(
                label = "Password",
                value = uiState.password,
                placeholder = "Create a password",
                placeholderColor = placeholderColor,
                colors = textFieldColors,
                isPassword = true,
                isPasswordVisible = uiState.isPasswordVisible,
                onTogglePasswordVisibility = { onAction(MemberRegAction.OnTogglePasswordVisibility) },
                onValueChange = { onAction(MemberRegAction.OnPasswordChanged(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LabeledField(
                label = "Confirm Password",
                value = uiState.confirmPassword,
                placeholder = "Re-enter your password",
                placeholderColor = placeholderColor,
                colors = textFieldColors,
                isPassword = true,
                isPasswordVisible = uiState.isConfirmPasswordVisible,
                onTogglePasswordVisibility = {
                    onAction(MemberRegAction.OnToggleConfirmPasswordVisibility)
                },
                onValueChange = { onAction(MemberRegAction.OnConfirmPasswordChanged(it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            uiState.errorMessage?.let { message ->
                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = { onAction(MemberRegAction.OnRegisterClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                enabled = uiState.isRegisterEnabled && !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Register",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Text(
                    text = "Already have an account? ",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 15.sp
                )
                Text(
                    modifier = Modifier.clickable { onAction(MemberRegAction.OnLoginClick) },
                    text = "Login",
                    color = accentColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
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

@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun MemberRegScreenPreview() {
    FundForGoalsTheme {
        MemberRegScreen(
            uiState = MemberRegUiState(),
            onAction = {}
        )
    }
}