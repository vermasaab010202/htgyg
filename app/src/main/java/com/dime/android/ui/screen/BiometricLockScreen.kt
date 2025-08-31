package com.dime.android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.dime.android.util.BiometricAuthManager

@Composable
fun BiometricLockScreen(
    onAuthenticationSuccess: () -> Unit,
    onAuthenticationError: (String) -> Unit
) {
    val context = LocalContext.current
    val biometricManager = remember { BiometricAuthManager() }
    var isAuthenticating by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (context is FragmentActivity) {
            isAuthenticating = true
            biometricManager.authenticate(
                activity = context,
                title = "Unlock Dime",
                subtitle = "Use your fingerprint or face to unlock the app",
                onSuccess = {
                    isAuthenticating = false
                    onAuthenticationSuccess()
                },
                onError = { error ->
                    isAuthenticating = false
                    errorMessage = error
                    onAuthenticationError(error)
                },
                onFailed = {
                    isAuthenticating = false
                    errorMessage = "Authentication failed. Please try again."
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Fingerprint,
            contentDescription = "Biometric authentication",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Dime",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your personal finance tracker is locked",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (isAuthenticating) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Authenticating...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Button(
                onClick = {
                    if (context is FragmentActivity) {
                        isAuthenticating = true
                        errorMessage = null
                        biometricManager.authenticate(
                            activity = context,
                            title = "Unlock Dime",
                            subtitle = "Use your fingerprint or face to unlock the app",
                            onSuccess = {
                                isAuthenticating = false
                                onAuthenticationSuccess()
                            },
                            onError = { error ->
                                isAuthenticating = false
                                errorMessage = error
                                onAuthenticationError(error)
                            },
                            onFailed = {
                                isAuthenticating = false
                                errorMessage = "Authentication failed. Please try again."
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Unlock with Biometrics")
            }
        }

        errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
