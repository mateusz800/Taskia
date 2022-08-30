package com.mabn.taskia.domain.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mabn.taskia.R
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class UncompletedTasksNotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val taskRepository: TaskRepository
) :
    Worker(context, workerParams) {
    private val channelId = "uncompleted tasks"

    private val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(context.getText(R.string.uncompleted_tasks))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    override fun doWork(): Result {
        createNotificationChannel(context = context)
        var uncompletedCount = 0
        runBlocking {
            uncompletedCount = taskRepository.getTodayTasks().first()
                .count { !it.task.status }
        }
        if (uncompletedCount > 0) {
            pushNotification(uncompletedCount)
        }
        return Result.success()
    }

    private fun pushNotification(uncompletedCount: Int) {
        builder.setContentText(
            context.resources.getQuantityString(
                R.plurals.uncompleted_tasks_with_count,
                uncompletedCount,
                uncompletedCount
            )
        )
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(LocalDate.now().dayOfYear, builder.build())
        }
    }


    private fun createNotificationChannel(context: Context) {
        val name = context.getString(R.string.uncompleted_tasks)
        val descriptionText = "channel description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                ContextCompat.getSystemService(context, NotificationManager::class.java)!!
            notificationManager.createNotificationChannel(channel)
        }
    }
}