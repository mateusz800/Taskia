package com.mabn.taskia.ui.taskForm

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mabn.taskia.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SaveButton(enabled: Boolean = true, saveFun: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    IconButton(
        modifier = Modifier.testTag("save_task_button"),
        onClick = {
            saveFun()
            keyboardController?.hide()
        },
        enabled = enabled
    ) {
        Icon(
            Icons.Default.Save,
            contentDescription = stringResource(id = R.string.save),
            tint = MaterialTheme.colors.onBackground
        )
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