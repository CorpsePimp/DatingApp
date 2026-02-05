package com.example.datingapp.presentation.ui.auth.login

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.datingapp.presentation.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit = {},
    onLoginSuccess: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

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
                .padding(24.dp)
                .graphicsLayer {
                    alpha = animatedAlpha
                    translationY = animatedTranslationY
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header
            Text(
                text = "💕",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = "Sign in to continue",
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
                        placeholder = "Enter your password",
                        leadingIcon = Icons.Rounded.Lock,
                        trailingIcon = if (passwordVisible) Icons.Rounded.Info else Icons.Rounded.Lock,
                        onTrailingIconClick = { passwordVisible = !passwordVisible },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                // Handle login
                            }
                        )
                    )

                    // Forgot Password
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "Forgot Password?",
                            style = MaterialTheme.typography.bodySmall,
                            color = DeepPink,
                            modifier = Modifier.clickable { /* Handle forgot password */ }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Login Button
                    GradientButton(
                        text = "Sign In",
                        onClick = {
                            isLoading = true
                            focusManager.clearFocus()
                            // Simulate API call
                            onLoginSuccess()
                        },
                        isLoading = isLoading,
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
                        text = "Continue with VK",
                        icon = Icons.Rounded.AccountCircle, // Replace with VK icon
                        backgroundColor = VKBlue,
                        onClick = { onLoginSuccess() },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SocialLoginButton(
                        text = "Continue with Mail.ru",
                        icon = Icons.Rounded.Email, // Replace with Mail.ru icon
                        backgroundColor = MailRuBlue,
                        onClick = { onLoginSuccess() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Prompt
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DeepPink,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp)),
        color = GlassWhite.copy(alpha = 0.7f),
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.8f),
                            Color.White.copy(alpha = 0.6f)
                        )
                    )
                )
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder, color = TextHint) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = if (value.isEmpty()) TextHint else DeepPink
            )
        },
        trailingIcon = trailingIcon?.let {
            {
                IconButton(onClick = { onTrailingIconClick?.invoke() }) {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                }
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DeepPink,
            unfocusedBorderColor = TextHint.copy(alpha = 0.3f),
            focusedLabelColor = DeepPink,
            unfocusedLabelColor = TextSecondary,
            cursorColor = DeepPink,
            focusedContainerColor = Color.White.copy(alpha = 0.5f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.3f)
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        targetValue = if (interactionSource.collectIsPressedAsState().value) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .height(56.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp),
        interactionSource = interactionSource
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(DeepPink, MediumViolet)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SocialLoginButton(
    text: String,
    icon: ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        targetValue = if (interactionSource.collectIsPressedAsState().value) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        interactionSource = interactionSource
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    DatingAppTheme {
        LoginScreen()
    }
}
