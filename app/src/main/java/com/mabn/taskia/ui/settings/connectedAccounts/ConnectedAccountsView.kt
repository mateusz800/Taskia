package com.mabn.taskia.ui.settings.connectedAccounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.AccountType
import com.mabn.taskia.domain.model.ConnectedAccount
import com.mabn.taskia.ui.common.AddNewButton
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
            ModalBottomSheetLayout(
                sheetState = modalBottomSheetState,
                sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                sheetBackgroundColor = MaterialTheme.colors.background,
                sheetContent = {
                    LazyColumn {
                        items(AccountType.values()) {
                            Button(onClick = { viewModel.addNewAccount(it) }) {
                                Text(it.title)
                            }
                        }
                    }
                }) {
                AccountsList(accountsList.value) {
                    coroutineScope.launch {
                        modalBottomSheetState.show()
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountsList(accounts: List<ConnectedAccount>?, addNew: () -> Unit) {
    LazyColumn {
        if (!accounts.isNullOrEmpty()) {
            items(accounts) {
                Column {
                    Text(it.type.title)
                    Text(it.userIdentifier)
                }

            }
        }
        item {
            AddNewButton(text = stringResource(id = R.string.add_new_account)) {
                addNew()
            }
        }
    }
}