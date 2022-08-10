package com.mabn.taskia.ui.taskForm.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.ui.common.CustomTextField
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TitleTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    onDoneKeyClick: () -> Unit,
    isVisible: Boolean = true,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(isVisible) {
        if (isVisible && value.isBlank()) {
            keyboardController?.show()
            delay(10)
            focusRequester.requestFocus()
        }

    }
    CustomTextField(
        value = value,
        onValueChange = onValueChanged,
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp, start = 5.dp, top = 10.dp)
            .focusRequester(focusRequester)
            .testTag("title_input"),
        /*
    .onKeyEvent {
        if (it.key == Key.Enter && value.isNotBlank()) {
            onDoneKeyClick.invoke()
            keyboardController?.hide()
        }
        true
    },

         */

        placeholderText = stringResource(id = R.string.task_name),
        underline = true,
        keyboardActions = KeyboardActions(onDone = {
            onDoneKeyClick()
            keyboardController?.hide()
        }, onPrevious = {
            println("previous")
        }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    )
}

@Preview
@Composable
private fun TitleTextField_Preview() {
    MaterialTheme {
        Surface {
            TitleTextField(value = "Task", onValueChanged = {}, onDoneKeyClick = { })
        }
    }
}