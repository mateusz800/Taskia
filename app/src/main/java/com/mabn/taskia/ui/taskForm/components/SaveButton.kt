package com.mabn.taskia.ui.taskForm.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SaveButton(enabled: Boolean = true, withText: Boolean = false, saveFun: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    IconButton(
        modifier = Modifier.testTag("save_task_button"),
        onClick = {
            saveFun()
            keyboardController?.hide()
        },
        enabled = enabled
    ) {
        Row {
            if (withText) {
                BigContent()
            } else {
                SaveIcon()
            }
        }

    }
}


@Composable
private fun SaveIcon() {
    Icon(
        Icons.Default.Save,
        contentDescription = stringResource(id = R.string.save),
        tint = MaterialTheme.colors.onBackground
    )
}

@Composable
private fun BigContent() {
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(5.dp)
    ) {
        SaveIcon()
        Text(stringResource(id = R.string.save), modifier = Modifier.padding(start = 10.dp))
    }

}

@Preview
@Composable
private fun SaveButton_Preview() {
    MaterialTheme {
        Surface {
            SaveButton {}
        }
    }
}

@Preview
@Composable
private fun SaveButton_Big_Preview() {
    MaterialTheme {
        Surface {
            SaveButton(withText = true) {}
        }
    }
}