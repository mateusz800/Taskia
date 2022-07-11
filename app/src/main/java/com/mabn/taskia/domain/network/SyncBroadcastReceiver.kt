package com.mabn.taskia.domain.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mabn.taskia.domain.notification.UncompletedTasksNotificationWorker
import java.util.concurrent.TimeUnit

class SyncBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val periodicWorkRequest = PeriodicWorkRequestBuilder<UncompletedTasksNotificationWorker>(
            15,
            TimeUnit.MINUTES
        ).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "syncTasks",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }
}