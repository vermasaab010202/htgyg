package com.dime.android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    var darkMode by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var biometricEnabled by remember { mutableStateOf(false) }
    var showCents by remember { mutableStateOf(true) }
    var hapticFeedback by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Appearance Section
        item {
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.Palette,
                title = "Dark Mode",
                subtitle = "Use dark theme",
                trailing = {
                    Switch(
                        checked = darkMode,
                        onCheckedChange = { darkMode = it }
                    )
                }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.MonetizationOn,
                title = "Show Cents",
                subtitle = "Display decimal places in amounts",
                trailing = {
                    Switch(
                        checked = showCents,
                        onCheckedChange = { showCents = it }
                    )
                }
            )
        }

        // Security Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Security",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.Fingerprint,
                title = "Biometric Lock",
                subtitle = "Secure app with fingerprint/face",
                trailing = {
                    Switch(
                        checked = biometricEnabled,
                        onCheckedChange = { biometricEnabled = it }
                    )
                }
            )
        }

        // Notifications Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Expense Reminders",
                subtitle = "Get reminded to log expenses",
                trailing = {
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                }
            )
        }

        // General Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "General",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.Vibration,
                title = "Haptic Feedback",
                subtitle = "Feel vibrations on interactions",
                trailing = {
                    Switch(
                        checked = hapticFeedback,
                        onCheckedChange = { hapticFeedback = it }
                    )
                }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.Category,
                title = "Manage Categories",
                subtitle = "Add, edit, or delete categories",
                onClick = { /* Navigate to category management */ }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.ImportExport,
                title = "Export Data",
                subtitle = "Export your data as CSV",
                onClick = { /* Implement export functionality */ }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.CloudUpload,
                title = "Backup & Sync",
                subtitle = "Sync data across devices",
                onClick = { /* Implement cloud sync */ }
            )
        }

        // About Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.Info,
                title = "About Dime",
                subtitle = "Version 1.0.0",
                onClick = { /* Show about dialog */ }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.PrivacyTip,
                title = "Privacy Policy",
                subtitle = "Read our privacy policy",
                onClick = { /* Open privacy policy */ }
            )
        }
    }
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (onClick != null) {
                        Modifier.clickableWithoutRipple { onClick() }
                    } else {
                        Modifier
                    }
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            trailing?.invoke()
        }
    }
}

@Composable
fun Modifier.clickableWithoutRipple(onClick: () -> Unit): Modifier {
    return this.clickable(
        indication = null,
        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    ) {
        onClick()
    }
}
