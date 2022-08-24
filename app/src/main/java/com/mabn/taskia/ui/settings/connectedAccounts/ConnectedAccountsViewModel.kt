package com.mabn.taskia.ui.settings.connectedAccounts

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mabn.taskia.domain.model.AccountType
import com.mabn.taskia.domain.model.ConnectedAccount
import com.mabn.taskia.domain.persistence.repository.ConnectedAccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectedAccountsViewModel @Inject constructor(
    private val connectedAccountRepository: ConnectedAccountRepository,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {
    private val _auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _connectedAccounts = MutableLiveData<List<ConnectedAccount>>()
    val connectedAccounts: LiveData<List<ConnectedAccount>> = _connectedAccounts

    private val _connectNewAccount = MutableStateFlow<Pair<AccountType, Intent>?>(null)
    val connectNewAccount: StateFlow<Pair<AccountType, Intent>?> = _connectNewAccount

    init {
        collectAccounts()
    }

    fun addNewAccount(accountType: AccountType) {
        viewModelScope.launch(Dispatchers.IO) {
            googleSignInClient.signOut()
            _connectNewAccount.emit(Pair(accountType, googleSignInClient.signInIntent))
        }
    }

    private fun saveNewAccount(accountType: AccountType, token: String, userIdentifier: String) {
        viewModelScope.launch(Dispatchers.IO) {
            connectedAccountRepository.insert(
                ConnectedAccount(
                    type = accountType,
                    token = token,
                    userIdentifier = userIdentifier
                )
            )
        }
    }

    private fun collectAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            connectedAccountRepository.getAll().collect {
                _connectedAccounts.postValue(it)
            }
        }
    }

    fun firebaseAuthWithGoogle(activity: Activity, account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        if (_auth.currentUser == null) {
            _auth.signInWithCredential(credential)
                .addOnSuccessListener(activity) {
                    saveNewAccount(AccountType.GOOGLE, account.idToken!!, account.email!!)
                    _auth.currentUser!!.linkWithCredential(credential)
                }
                .addOnFailureListener(activity) {
                    println("Exception occurred")
                }
        } else {
            _auth.currentUser!!.linkWithCredential(credential)
            saveNewAccount(AccountType.GOOGLE, account.idToken!!, account.email!!)
        }
    }

    fun disconnectAccount(acc: ConnectedAccount) {
        viewModelScope.launch(Dispatchers.IO) {
            connectedAccountRepository.delete(acc)
        }
    }
}