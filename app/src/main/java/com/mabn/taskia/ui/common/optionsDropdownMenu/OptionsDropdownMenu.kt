package com.mabn.taskia.ui.common.optionsDropdownMenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.mabn.taskia.R
import com.mabn.taskia.ui.common.AlertButton

@Composable
fun OptionsDropdownMenu(expanded: Boolean, onDismissRequest: () -> Unit) {
    val viewModel: OptionsDropdownMenViewModel = hiltViewModel()
    val showAlertDialog = remember { mutableStateOf(false) }
    val deleteAllTasksAvailable = viewModel.deleteAllTaskButtonAvailable.observeAsState(true)
    val deleteCompletedTasksAvailable =
        viewModel.deleteCompleteTaskButtonAvailable.observeAsState(true)

    val dismiss = {
        onDismissRequest()
        showAlertDialog.value = false
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            enabled = deleteAllTasksAvailable.value,
            onClick = {
                viewModel.queueTask {
                    viewModel.removeAllTasks()
                    dismiss()
                }
                showAlertDialog.value = true
            }) {
            Text(stringResource(id = R.string.remove_all))
        }
        Divider()
        DropdownMenuItem(
            enabled = deleteCompletedTasksAvailable.value,
            onClick = {
                viewModel.queueTask {
                    viewModel.removeAllCompletedTasks()
                    dismiss()
                }
                showAlertDialog.value = true
            }) {
            Text(stringResource(id = R.string.remove_completed))
        }
        if (showAlertDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    showAlertDialog.value = false
                    onDismissRequest()
                },
                properties = DialogProperties(),
                title = {
                    Text(stringResource(id = R.string.sure_perform_operation))
                },
                buttons = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AlertButton(
                            stringResource(id = R.string.no),
                            onClick = {
                                showAlertDialog.value = false
                                onDismissRequest()
                            }
                        )
                        AlertButton(
                            stringResource(id = R.string.yes),
                            onClick = { viewModel.execQueuedTask() }
                        )

                    }

                }
            )
        }
    }
}





@Preview
@Composable
private fun OptionsDropdownMenu_Preview() {
    MaterialTheme {
        OptionsDropdownMenu(expanded = true) {

        }
    }
}