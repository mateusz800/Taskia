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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


@HiltAndroidApp
class App : Application(),
    Configuration.Provider {


    @Inject
    lateinit var workerFactory: CustomWorkerFactory

    private val applicationScope = MainScope()


    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        applicationScope.launch {
            scheduleEndDayAlarm()
            scheduleSync()
        }
    }


    private suspend fun scheduleEndDayAlarm() {
        withContext(Dispatchers.IO) {
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 18)
            val intent = Intent(applicationContext, NotificationBroadcastReceiver::class.java)
            val pendingIntent =
                PendingIntent.getBroadcast(
                    this@App,
                    0,
                    intent,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE
                    else PendingIntent.FLAG_CANCEL_CURRENT
                )
            (this@App.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
                .setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    //calendar.timeInMillis,
                    System.currentTimeMillis(),
                    pendingIntent
                )
        }
    }

    private suspend fun scheduleSync() {
        withContext(Dispatchers.IO) {
            val intent = Intent(applicationContext, SyncBroadcastReceiver::class.java)
            val pendingIntent =
                PendingIntent.getBroadcast(
                    this@App,
                    0,
                    intent,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE
                    else PendingIntent.FLAG_CANCEL_CURRENT
                )
            (this@App.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
                .setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    //calendar.timeInMillis,
                    System.currentTimeMillis(),
                    pendingIntent
                )
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}