package com.mabn.taskia.domain.network

import android.database.sqlite.SQLiteConstraintException
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.AccountType
import com.mabn.taskia.domain.model.ConnectedAccount
import com.mabn.taskia.domain.model.GoogleAccountData
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.network.google.tasks.GoogleTasksApiClient
import com.mabn.taskia.domain.persistence.repository.ConnectedAccountRepository
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import com.mabn.taskia.domain.util.ContextProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.OffsetDateTime
import javax.inject.Inject


class TasksSynchronizer @Inject constructor(
    private val contextProvider: ContextProvider,
    private val connectedAccountRepository: ConnectedAccountRepository,
    private val googleSignInClient: GoogleSignInClient,
    private val googleTasksApiClient: GoogleTasksApiClient,
    private val taskRepository: TaskRepository
) {
    private var _connectedAccount: List<ConnectedAccount> = listOf()
    private val _auth = FirebaseAuth.getInstance()

    init {
        observeConnectedAccounts()
    }

    suspend fun sync() {
        _connectedAccount.forEach {
            when (it.type) {
                AccountType.GOOGLE -> {
                    val account = runBlocking { syncGoogleTaskLists(it) }
                    syncGoogleTasks(account = account)
                }

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

    private suspend fun refreshGoogleToken(
        account: ConnectedAccount,
        nextAction: suspend (account: ConnectedAccount) -> Unit = {}
    ): ConnectedAccount {
        var accountResult = MutableStateFlow<ConnectedAccount?>(null)
        googleSignInClient.silentSignIn().addOnCompleteListener {
            runBlocking {
                launch(Dispatchers.IO) {
                    val token = it.result.serverAuthCode!!
                    val tokenResponse = GoogleAuthorizationCodeTokenRequest(
                        NetHttpTransport(),
                        GsonFactory.getDefaultInstance(),  // Very important: Explicitly specify this new endpoint.
                        contextProvider.getString(R.string.google_client_id),
                        contextProvider.getString(R.string.google_client_secret),
                        token,
                        "" /* redirectUri, must be blank with auth code coming from Android */
                    ).execute()
                    account.refreshToken = tokenResponse.refreshToken
                    account.token = tokenResponse.accessToken
                    launch(Dispatchers.IO) {
                        nextAction(account)
                    }
                    accountResult.emit(account)
                }
            }
        }
        return accountResult.first { it != null }!!
    }

    private suspend fun syncGoogleTaskLists(account: ConnectedAccount): ConnectedAccount {
        val gson = Gson()
        val response =
            googleTasksApiClient.getTaskLists(
                auth = "Bearer ${account.token}",
            )
        if (response.isSuccessful) {
            if (account.data == null) {
                account.data = gson.toJson(GoogleAccountData())
            }
            val items = response.body()?.items ?: listOf()
            val googleAccountData = gson.fromJson(account.data, GoogleAccountData::class.java)
            googleAccountData.taskListsIdList.addAll(
                items.map { it.id }.toTypedArray()
            )
            account.data = gson.toJson(googleAccountData)
            connectedAccountRepository.update(account)

        } else if (response.code() == 401) {
            return refreshGoogleToken(
                account = account,
                nextAction = { acc -> syncGoogleTaskLists(acc) })
        }
        return account
    }

    private suspend fun syncGoogleTasks(account: ConnectedAccount) {
        val gson = Gson()
        val data: GoogleAccountData =
            gson.fromJson(account.data, GoogleAccountData::class.java) ?: GoogleAccountData()
        data.taskListsIdList.forEach {
            val response = googleTasksApiClient.getTasks(
                auth = "Bearer ${account.token}",
                taskListId = it
            )
            if (response.isSuccessful) {
                val responseData = response.body()
                responseData?.items?.forEach { task ->
                    if (task.title.isNotBlank()) {
                        val newTask = Task(
                            title = task.title,
                            status = task.status.contentEquals("completed", true),
                            endDate = if (task.due != null) OffsetDateTime.parse(task.due)
                                .toLocalDateTime() else null,
                            googleId = task.id
                        )
                        try {
                            taskRepository.insertAll(newTask)
                        } catch (e: SQLiteConstraintException) {
                            // do nothing
                        }
                    }
                }
            } else if (response.code() == 401) {
                refreshGoogleToken(account = account, nextAction = { acc -> syncGoogleTasks(acc) })
            }
        }
    }
}