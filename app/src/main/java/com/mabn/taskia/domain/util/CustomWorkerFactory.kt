package com.mabn.taskia.domain.util

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.mabn.taskia.domain.notification.UncompletedTasksNotificationWorker
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import javax.inject.Inject

class CustomWorkerFactory @Inject constructor(private val taskRepository: TaskRepository) :
    WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return UncompletedTasksNotificationWorker(appContext, workerParameters, taskRepository)

    }
}