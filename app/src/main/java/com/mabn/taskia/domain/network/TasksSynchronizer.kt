package com.mabn.taskia.domain.network

import com.mabn.taskia.domain.model.*
import com.mabn.taskia.domain.network.google.tasks.GoogleTasksSynchronizer
import com.mabn.taskia.domain.persistence.repository.ConnectedAccountRepository
import com.mabn.taskia.domain.persistence.repository.SyncDataRepository
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import kotlinx.coroutines.*
import javax.inject.Inject


class TasksSynchronizer @Inject constructor(
    private val connectedAccountRepository: ConnectedAccountRepository,
    private val syncDataRepository: SyncDataRepository,
    private val taskRepository: TaskRepository,
    private val googleTasksSynchronizer: GoogleTasksSynchronizer,

    ) {
    private var _connectedAccount: List<ConnectedAccount> = listOf()

    init {
        observeConnectedAccounts()
    }

    fun sync() {
        _connectedAccount.forEach {
            when (it.type) {
                AccountType.GOOGLE -> googleTasksSynchronizer.sync(it) { task, operation ->
                    syncDataRepository.insert(SyncData(taskId = task.id, operation = operation))
                }
                AccountType.UNKNOWN -> {
                }
            }
        }
    }

    suspend fun syncQueue() {
        syncDataRepository.getAll().forEach {
            val task = taskRepository.getById(it.taskId)
            when (it.operation) {
                SyncDataOperation.UPDATE -> when (task?.provider?.type) {
                    AccountType.GOOGLE -> googleTasksSynchronizer.updateGoogleTask(
                        task.provider,
                        task
                    )
                    else -> {}
                }
                SyncDataOperation.DELETE -> when (task?.provider?.type) {
                    AccountType.GOOGLE -> runBlocking {
                        if (googleTasksSynchronizer.deleteGoogleTask(task)) {
                            taskRepository.delete(task)
                        } else {
                            task.isRemoved = true
                            taskRepository.update(task)
                        }
                    }
                    else -> {
                        if (task != null) {
                            taskRepository.delete(task)
                        }
                    }
                }
                SyncDataOperation.INSERT -> {}
            }

        }

    }

    private fun observeConnectedAccounts() {
        CoroutineScope(Dispatchers.IO).launch {
            connectedAccountRepository.getAll().collect {
                _connectedAccount = it
            }
        }
    }

    fun updateTask(task: Task) {
        if (task.googleId != null && task.googleTaskList != null) {
            val account = _connectedAccount.find { it.type == AccountType.GOOGLE }
            if (account != null) {
                GlobalScope.launch(Dispatchers.IO) {
                    googleTasksSynchronizer.updateGoogleTask(
                        account = account,
                        listId = task.googleTaskList,
                        taskId = task.googleId!!,
                        task = task,
                    )
                }
            }
        }
    }

    suspend fun delete(task: Task) {
        when (task.provider?.type) {
            AccountType.GOOGLE -> googleTasksSynchronizer.deleteGoogleTask(task)
            else -> {
                taskRepository.delete(task)
            }
        }

    }

    suspend fun insert(task: Task) {
        when (task.provider?.type) {
            AccountType.GOOGLE -> googleTasksSynchronizer.insertTask(task)
            else -> {}
        }
    }


}