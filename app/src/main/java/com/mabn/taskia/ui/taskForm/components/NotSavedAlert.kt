package com.mabn.taskia.ui.taskForm.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.mabn.taskia.R
import com.mabn.taskia.ui.common.AlertButton


@Composable
fun NotSavedAlert(saveFun: () -> Unit, dismissFun: () -> Unit, discardFun: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            dismissFun()
        },
        properties = DialogProperties(),
        title = {
            Text(stringResource(id = R.string.unsaved_changes))
        },
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                AlertButton(
                    stringResource(id = R.string.discard),
                    onClick = {
                        discardFun()
                    },
                    modifier = Modifier.padding(end = 10.dp)
                )
                AlertButton(
                    stringResource(id = R.string.save),
                    onClick = { saveFun() }
                )
            }


        }
    )
}