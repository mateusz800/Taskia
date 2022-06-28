package com.mabn.taskia.ui.settings.connectedAccounts

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.AccountType
import com.mabn.taskia.domain.model.ConnectedAccount
import com.mabn.taskia.ui.common.AddNewButton
import com.mabn.taskia.ui.common.DeleteBackground
import com.mabn.taskia.ui.settings.SettingsButton
import com.mabn.taskia.ui.theme.DoItTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConnectedAccountsView(viewModel: ConnectedAccountsViewModel) {
    val modalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden
        )
    val accountsList = viewModel.connectedAccounts.observeAsState()
    val coroutineScope = rememberCoroutineScope()
    DoItTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            BackHandler(enabled = modalBottomSheetState.isVisible) {
                coroutineScope.launch {
                    modalBottomSheetState.hide()
                }
            }
            ModalBottomSheetLayout(
                sheetState = modalBottomSheetState,
                sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                sheetBackgroundColor = MaterialTheme.colors.background,
                sheetContent = {
                    Column(modifier = Modifier.defaultMinSize(minHeight = 300.dp)) {
                        AccountType.values().forEach {
                            val sameAccountType =
                                accountsList.value?.filter { connectedAccount -> connectedAccount.type == it }
                            SettingsButton(text = it.title, onClick = {
                                viewModel.addNewAccount(it)
                                coroutineScope.launch {
                                    modalBottomSheetState.hide()
                                }
                            }, icon = painterResource(id = R.drawable.google_logo),
                            enabled = sameAccountType.isNullOrEmpty())


                        }
                    }
                }) {
                AccountsList(
                    accountsList.value,
                    removeItemFunc = { acc -> viewModel.disconnectAccount(acc) },
                    addNew = {
                        coroutineScope.launch {
                            modalBottomSheetState.show()
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun AccountsList(
    accounts: List<ConnectedAccount>?,
    addNew: () -> Unit,
    removeItemFunc: (ConnectedAccount) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(20.dp)) {
        if (!accounts.isNullOrEmpty()) {
            items(accounts) {
                AccountView(it, removeItemFunc = removeItemFunc)
            }
        }
        item {
            AddNewButton(text = stringResource(id = R.string.add_new_account)) {
                addNew()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AccountView(account: ConnectedAccount, removeItemFunc: (ConnectedAccount) -> Unit) {
    val dismissState = rememberDismissState(confirmStateChange = {
        if (it == DismissValue.DismissedToEnd) {
            removeItemFunc(account)
        }
        false
    })
    SwipeToDismiss(
        state = dismissState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        background = {
            DeleteBackground()
        },
        directions = setOf(DismissDirection.StartToEnd)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxWidth()
        ) {
            Icon(
                painterResource(
                    id = when (account.type) {
                        AccountType.GOOGLE -> R.drawable.google_logo
                    }
                ), null,
                modifier = Modifier
                    .height(30.dp)
                    .padding(end = 10.dp)
            )
            Column {
                Text(account.type.title, style = MaterialTheme.typography.h3)
                Text(account.userIdentifier, style = MaterialTheme.typography.caption)

            }
        }
    }
}