package com.mabn.taskia.domain.network

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SyncTasksWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val tasksSynchronizer: TasksSynchronizer
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        GlobalScope.launch {
            tasksSynchronizer.syncQueue()
        }
        return Result.success()
    }
}