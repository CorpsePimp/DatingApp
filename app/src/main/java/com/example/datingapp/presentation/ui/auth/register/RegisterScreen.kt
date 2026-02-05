package com.example.datingapp.presentation.ui.auth.register

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.datingapp.presentation.ui.auth.login.*
import com.example.datingapp.presentation.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var acceptTerms by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    // Animation states
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "alpha"
    )

    val animatedTranslationY by animateFloatAsState(
        targetValue = if (visible) 0f else 50f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "translationY"
    )

    // Validation
    val isPasswordMatch = password == confirmPassword && confirmPassword.isNotEmpty()
    val isFormValid = name.isNotEmpty() && email.isNotEmpty() &&
                     password.isNotEmpty() && isPasswordMatch && acceptTerms

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BackgroundStart, BackgroundEnd)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp)
                .graphicsLayer {
                    alpha = animatedAlpha
                    translationY = animatedTranslationY
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Header
            Text(
                text = "💕",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = "Sign up to get started",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Glass Card
            GlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Name Field
                    AuthTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Full Name",
                        placeholder = "Enter your name",
                        leadingIcon = Icons.Rounded.Person,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email Field
                    AuthTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        placeholder = "Enter your email",
                        leadingIcon = Icons.Rounded.Email,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Field
                    AuthTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        placeholder = "Create a password",
                        leadingIcon = Icons.Rounded.Lock,
                        trailingIcon = if (passwordVisible) Icons.Rounded.Info else Icons.Rounded.Lock,
                        onTrailingIconClick = { passwordVisible = !passwordVisible },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    // Password strength indicator
                    AnimatedVisibility(
                        visible = password.isNotEmpty(),
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        PasswordStrengthIndicator(password = password)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirm Password Field
                    AuthTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Confirm Password",
                        placeholder = "Re-enter your password",
                        leadingIcon = Icons.Rounded.Lock,
                        trailingIcon = if (confirmPasswordVisible) Icons.Rounded.Info else Icons.Rounded.Lock,
                        onTrailingIconClick = { confirmPasswordVisible = !confirmPasswordVisible },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        )
                    )

                    // Password match indicator
                    AnimatedVisibility(
                        visible = confirmPassword.isNotEmpty(),
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, start = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isPasswordMatch) Icons.Rounded.CheckCircle else Icons.Rounded.Close,
                                contentDescription = null,
                                tint = if (isPasswordMatch) SuccessGreen else ErrorRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isPasswordMatch) "Passwords match" else "Passwords don't match",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isPasswordMatch) SuccessGreen else ErrorRed
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Terms and Conditions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = acceptTerms,
                            onCheckedChange = { acceptTerms = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = DeepPink,
                                uncheckedColor = TextSecondary
                            )
                        )
                        Text(
                            text = "I agree to the ",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Text(
                            text = "Terms & Conditions",
                            style = MaterialTheme.typography.bodySmall,
                            color = DeepPink,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { /* Show T&C */ }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Register Button
                    GradientButton(
                        text = "Create Account",
                        onClick = {
                            isLoading = true
                            focusManager.clearFocus()
                            // Simulate API call
                            onRegisterSuccess()
                        },
                        isLoading = isLoading,
                        enabled = isFormValid,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = TextHint.copy(alpha = 0.3f))
                        Text(
                            text = "OR",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f), color = TextHint.copy(alpha = 0.3f))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Social Login Buttons
                    SocialLoginButton(
                        text = "Sign up with VK",
                        icon = Icons.Rounded.AccountCircle, // Replace with VK icon
                        backgroundColor = VKBlue,
                        onClick = { onRegisterSuccess() },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SocialLoginButton(
                        text = "Sign up with Mail.ru",
                        icon = Icons.Rounded.Email, // Replace with Mail.ru icon
                        backgroundColor = MailRuBlue,
                        onClick = { onRegisterSuccess() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign In Prompt
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Text(
                    text = "Sign In",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DeepPink,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PasswordStrengthIndicator(password: String) {
    val strength = calculatePasswordStrength(password)
    val color = when (strength) {
        PasswordStrength.WEAK -> ErrorRed
        PasswordStrength.MEDIUM -> MailRuOrange
        PasswordStrength.STRONG -> SuccessGreen
    }

    val progress by animateFloatAsState(
        targetValue = when (strength) {
            PasswordStrength.WEAK -> 0.33f
            PasswordStrength.MEDIUM -> 0.66f
            PasswordStrength.STRONG -> 1f
        },
        animationSpec = tween(durationMillis = 300),
        label = "progress"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 4.dp, end = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Password strength:",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            Text(
                text = strength.label,
                style = MaterialTheme.typography.bodySmall,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = color,
            trackColor = TextHint.copy(alpha = 0.2f),
        )
    }
}

enum class PasswordStrength(val label: String) {
    WEAK("Weak"),
    MEDIUM("Medium"),
    STRONG("Strong")
}

fun calculatePasswordStrength(password: String): PasswordStrength {
    var score = 0

    if (password.length >= 8) score++
    if (password.any { it.isUpperCase() }) score++
    if (password.any { it.isLowerCase() }) score++
    if (password.any { it.isDigit() }) score++
    if (password.any { !it.isLetterOrDigit() }) score++

    return when {
        score <= 2 -> PasswordStrength.WEAK
        score <= 3 -> PasswordStrength.MEDIUM
        else -> PasswordStrength.STRONG
    }
}
