package com.mabn.taskia.domain.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.hilt.work.HiltWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

@HiltWorker
class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, p1: Intent?) {
        val periodicWorkRequest = PeriodicWorkRequestBuilder<UncompletedTasksNotificationWorker>(
            1,
            TimeUnit.DAYS
        ).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "checkTasksOnDayEnd",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )

    }

}