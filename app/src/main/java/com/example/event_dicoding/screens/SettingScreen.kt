package com.example.event_dicoding.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.*
import com.example.event_dicoding.data.local.SettingPreferences
import com.example.event_dicoding.data.local.dataStore
import com.example.event_dicoding.data.worker.DailyReminderWorker
import com.example.event_dicoding.ui.setting.SettingViewModel
import com.example.event_dicoding.ui.setting.SettingViewModelFactory
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val pref = SettingPreferences.getInstance(context.dataStore)
    val viewModel: SettingViewModel = viewModel(factory = SettingViewModelFactory(pref))
    
    val isDarkMode by viewModel.getThemeSettings().collectAsState(initial = false)
    val isReminderActive by viewModel.getReminderSettings().collectAsState(initial = false)

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(isReminderActive) {
        if (isReminderActive) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            startDailyReminder(context)
        } else {
            cancelDailyReminder(context)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Peraturan", fontWeight = FontWeight.ExtraBold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingItem(
                title = "Dark Mode",
                subtitle = if (isDarkMode) "Tema Gelap Aktif" else "Tema Terang Aktif",
                checked = isDarkMode,
                onCheckedChange = { viewModel.saveThemeSetting(it) }
            )

            SettingItem(
                title = "Daily Reminder",
                subtitle = "Notifikasi event terdekat setiap hari",
                checked = isReminderActive,
                onCheckedChange = { viewModel.saveReminderSetting(it) }
            )
        }
    }
}

@Composable
fun SettingItem(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

private fun startDailyReminder(context: android.content.Context) {
    val workManager = WorkManager.getInstance(context)
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
    
    val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
        .setConstraints(constraints)
        .addTag("DailyReminder")
        .build()
    
    workManager.enqueueUniquePeriodicWork(
        "DailyReminder",
        ExistingPeriodicWorkPolicy.UPDATE,
        dailyWorkRequest
    )
}

private fun cancelDailyReminder(context: android.content.Context) {
    val workManager = WorkManager.getInstance(context)
    workManager.cancelAllWorkByTag("DailyReminder")
}
