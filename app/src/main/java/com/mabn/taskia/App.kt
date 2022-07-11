package com.mabn.taskia

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.Configuration
import com.mabn.taskia.domain.network.SyncBroadcastReceiver
import com.mabn.taskia.domain.notification.NotificationBroadcastReceiver
import com.mabn.taskia.domain.util.CustomWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import java.util.*
import javax.inject.Inject


@HiltAndroidApp
class App : Application(),
    Configuration.Provider {


    @Inject
    lateinit var workerFactory: CustomWorkerFactory


    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        scheduleEndDayAlarm()
        scheduleSync()

    }

    private fun scheduleEndDayAlarm() {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 18)
        val intent = Intent(applicationContext, NotificationBroadcastReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(
                this,
                0,
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE
                else PendingIntent.FLAG_CANCEL_CURRENT
            )
        (this.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
            .setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                //calendar.timeInMillis,
                System.currentTimeMillis(),
                pendingIntent
            )
    }

    private fun scheduleSync() {
        val intent = Intent(applicationContext, SyncBroadcastReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(
                this,
                0,
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE
                else PendingIntent.FLAG_CANCEL_CURRENT
            )
        (this.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
            .setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                //calendar.timeInMillis,
                System.currentTimeMillis(),
                pendingIntent
            )
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}