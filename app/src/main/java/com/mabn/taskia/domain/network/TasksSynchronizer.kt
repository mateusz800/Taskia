package com.mabn.taskia.domain.network

import com.google.firebase.auth.FirebaseAuth
import com.mabn.taskia.domain.model.AccountType
import com.mabn.taskia.domain.model.ConnectedAccount
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.network.google.tasks.GoogleTasksSynchronizer
import com.mabn.taskia.domain.persistence.repository.ConnectedAccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


class TasksSynchronizer @Inject constructor(
    private val connectedAccountRepository: ConnectedAccountRepository,
    private val googleTasksSynchronizer: GoogleTasksSynchronizer,

    ) {
    private var _connectedAccount: List<ConnectedAccount> = listOf()
    private val _auth = FirebaseAuth.getInstance()

    init {
        observeConnectedAccounts()
    }

    suspend fun sync() {
        _connectedAccount.forEach {
            when (it.type) {
                AccountType.GOOGLE -> googleTasksSynchronizer.sync(it)
            }
        }
    }

    private fun observeConnectedAccounts() {
        GlobalScope.launch(Dispatchers.IO) {
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
            else -> {}
        }
    }

    suspend fun insert(task: Task) {
        when (task.provider?.type) {
            AccountType.GOOGLE -> googleTasksSynchronizer.insertTask(task)
            else -> {}
        }
    }


}