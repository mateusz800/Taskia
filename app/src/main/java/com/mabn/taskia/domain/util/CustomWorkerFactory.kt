package com.mabn.taskia.domain.util

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.mabn.taskia.domain.network.SyncTasksWorker
import com.mabn.taskia.domain.network.TasksSynchronizer
import com.mabn.taskia.domain.notification.UncompletedTasksNotificationWorker
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import javax.inject.Inject

class CustomWorkerFactory @Inject constructor(
    private val taskRepository: TaskRepository,
    private val tasksSynchronizer: TasksSynchronizer
) :
    WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            SyncTasksWorker::class.java.name -> SyncTasksWorker(
                appContext,
                workerParameters,
                tasksSynchronizer
            )
            UncompletedTasksNotificationWorker::class.java.name -> UncompletedTasksNotificationWorker(
                appContext,
                workerParameters,
                taskRepository
            )
            else -> null
        }

    }
}