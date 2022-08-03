package com.mabn.taskia.ui.settings.connectedAccounts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.mabn.taskia.R
import com.mabn.taskia.ui.common.base.ActivityWithActionBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConnectedAccountsActivity : ActivityWithActionBar() {

    private lateinit var viewModel: ConnectedAccountsViewModel


    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            } else {
                println("Canceled")
                val data = result.data
                println(data.toString())
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.title = getString(R.string.connected_accounts)
        viewModel = ViewModelProvider(this).get(
            ConnectedAccountsViewModel::class.java
        )
        handleAddAccountRequest()
        setContent {
            ConnectedAccountsView(hiltViewModel())
        }
    }

    private fun handleAddAccountRequest() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.connectNewAccount.collect {
                if (it != null) {
                    signIn(it.second)
                }
            }
        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            viewModel.firebaseAuthWithGoogle(this, account)
        } catch (e: ApiException) {
            println("nie ok")
        }
    }


    private fun signIn(signInIntent: Intent) {
        resultLauncher.launch(signInIntent)
    }
    override fun onNavigateUp(): Boolean {
        finish()
        return true
    }
}