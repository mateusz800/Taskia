package com.mabn.taskia.ui.settings.connectedAccounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mabn.taskia.domain.model.AccountType
import com.mabn.taskia.domain.model.ConnectedAccount
import com.mabn.taskia.domain.persistence.repository.ConnectedAccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectedAccountsViewModel @Inject constructor(
    private val connectedAccountRepository: ConnectedAccountRepository
) : ViewModel() {

    private val _connectedAccounts = MutableLiveData<List<ConnectedAccount>>()
    val connectedAccounts: LiveData<List<ConnectedAccount>> = _connectedAccounts

    private val _connectNewAccount = MutableStateFlow<AccountType?>(null)
    val connectNewAccount: StateFlow<AccountType?> = _connectNewAccount

    init {
        collectAccounts()
    }

    fun addNewAccount(accountType: AccountType) {
        viewModelScope.launch(Dispatchers.IO) {
            _connectNewAccount.emit(accountType)
        }
    }

    fun saveNewAccount(accountType: AccountType, token: String, userIdentifier: String) {
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
}