package com.sunnah.app.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sunnah.app.data.NotificationSetting
import com.sunnah.app.viewmodel.MainViewModel

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    val settings by viewModel.settings.collectAsState(initial = emptyList())
    
    // Ensure default settings exist for display if database is empty
    val displaySettings = if (settings.isEmpty()) {
        listOf(
            NotificationSetting("Tahajjud", true, 3, 30),
            NotificationSetting("Sunrise", true, 6, 0),
            NotificationSetting("Midday", true, 13, 0),
            NotificationSetting("Sunset", true, 19, 0),
            NotificationSetting("Bedtime", true, 22, 0)
        )
    } else settings

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ProfileHeader()
        }

        item {
            Text(
                "Notification Times",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(displaySettings) { setting ->
            NotificationToggleRow(setting) {
                viewModel.updateSetting(setting.copy(isEnabled = it))
            }
        }

        item {
            Text(
                "AI Configuration",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            ApiKeyCard(viewModel)
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { viewModel.clearHistory() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Reset All Statistics")
            }
        }
    }
}

@Composable
fun ProfileHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Sunnah Seeker", style = MaterialTheme.typography.titleLarge)
                Text("Reviving the Traditions", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@Composable
fun NotificationToggleRow(setting: NotificationSetting, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(setting.type, fontWeight = FontWeight.Bold)
                Text(
                    String.format("%02d:%02d", setting.hour, setting.minute),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Switch(
            checked = setting.isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun ApiKeyCard(viewModel: MainViewModel) {
    val apiKey by viewModel.apiKey.collectAsState(initial = "")
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Gemini API Key", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                if (apiKey.isNullOrBlank()) "Not configured" else "••••••••" + (apiKey?.takeLast(4) ?: ""),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.saveApiKey("") },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (apiKey.isNullOrBlank()) "Configure" else "Reset Key")
            }
        }
    }
}
