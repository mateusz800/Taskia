package com.mabn.taskia.ui.taskForm.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Tag
import com.mabn.taskia.ui.common.CustomTextField

@Composable
fun Tags(
    tags: List<Tag>,
    addNewFun: (focusOnNew: Boolean) -> Unit,
    onTitleChanged: (Tag, String) -> Unit,
    isVisible: Boolean
) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.padding(vertical = 20.dp)) {
        Text(
            stringResource(id = R.string.tags),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        Column(Modifier) {
            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
            ) {
                Spacer(Modifier.width(20.dp))
                if (isVisible) {
                    tags.forEachIndexed { index, tag ->
                        TagInput(
                            tag = tag,
                            onTitleChanged = onTitleChanged,
                            onEnter = addNewFun,
                            focus = index == tags.size - 1 && tag.value.isEmpty()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TagInput(
    tag: Tag,
    onTitleChanged: (Tag, String) -> Unit,
    onEnter: (focusOnNext: Boolean) -> Unit,
    focus: Boolean = false
) {
    val focusRequester = FocusRequester()
    val isCurrentlyFocused = remember { mutableStateOf(true) }
    LaunchedEffect(focus) {
        if (focus) {
            focusRequester.requestFocus()
        }
    }
    Spacer(modifier = Modifier.width(10.dp))
    Box(
        modifier = Modifier
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = if (!isCurrentlyFocused.value && !tag.value.isNullOrBlank()) MaterialTheme.colors.onBackground else Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .scale(if (isCurrentlyFocused.value) 1f else 0.8f),
        contentAlignment = Alignment.Center
    ) {
        CustomTextField(
            value = if (tag.value != " ") tag.value else "",
            onValueChange = { onTitleChanged(tag, it) },
            style = MaterialTheme.typography.body2,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { onEnter.invoke(true) }
            ),
            placeholderText = stringResource(id = R.string.add_tag),
            alignCenter = !isCurrentlyFocused.value && tag.value.isNotBlank(),
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isCurrentlyFocused.value = it.isFocused
                    if (!it.isFocused) {
                        onEnter(false)
                                           }
                }
                .defaultMinSize(minWidth = 70.dp)
                .onKeyEvent {
                    if (it.key == Key.Enter) {
                        onEnter.invoke(true)
                    }
                    true
                },
        )
    }
}

@Preview
@Composable
private fun Tags_Preview() {
    val tags = listOf(
        Tag(value = "work"),
    )
    MaterialTheme {
        Tags(
            tags = tags,
            addNewFun = {},
            onTitleChanged = { _, _ -> },
            isVisible = true
        )
    }
}