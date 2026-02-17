package com.example.event_dicoding.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.event_dicoding.R
import com.example.event_dicoding.data.remote.retrofit.ApiConfig

class DailyReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val apiService = ApiConfig.getApiService()
            val response = apiService.getEvents(active = -1, limit = 1)
            
            if (response.listEvents.isNotEmpty()) {
                val event = response.listEvents[0]
                showNotification(event.name, event.beginTime)
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Event Terdekat: $title")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        const val CHANNEL_ID = "daily_reminder_channel"
        const val CHANNEL_NAME = "Daily Reminder"
        const val NOTIFICATION_ID = 1
    }
}
