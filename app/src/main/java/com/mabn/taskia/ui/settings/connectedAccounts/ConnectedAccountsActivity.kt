package com.mabn.taskia.ui.settings.connectedAccounts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.services.calendar.CalendarScopes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.AccountType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConnectedAccountsActivity : ComponentActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

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
        viewModel = ViewModelProvider(this).get(
            ConnectedAccountsViewModel::class.java
        )
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(CalendarScopes.CALENDAR))
            .requestEmail()
            .requestIdToken(getString(R.string.google_client_id))
            .build()
        auth = FirebaseAuth.getInstance()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        handleAddAccountRequest()
        setContent {
            ConnectedAccountsView(hiltViewModel())
        }
    }

    private fun handleAddAccountRequest() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.connectNewAccount.collect {
                when (it) {
                    AccountType.GOOGLE -> launch(Dispatchers.Main) { signInWithGoogle() }
                    null -> {}
                }
            }
        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            println("nie ok")
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener(this) { authResult ->
                viewModel.saveNewAccount(AccountType.GOOGLE, acct.idToken!!, acct.email!!)
                mGoogleSignInClient.signOut()
                Toast.makeText(
                    this, "Signed In.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener(this) { e ->
                Toast.makeText(
                    this, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        auth.currentUser
    }

    private fun signInWithGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }
}