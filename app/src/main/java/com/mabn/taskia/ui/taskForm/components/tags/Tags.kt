package com.mabn.taskia.ui.taskForm.components.tags

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Tag
import com.mabn.taskia.ui.taskForm.FormEvent
import com.mabn.taskia.ui.taskForm.components.Label

@Composable
fun Tags(tags: List<Tag>, onEvent: (FormEvent) -> Unit, isVisible: Boolean) {
    Tags(
        tags = tags,
        addNewFun = { forceFocus -> onEvent(FormEvent.AddNewTag(forceFocus)) },
        onTitleChanged = { tag, newValue -> onEvent(FormEvent.TagValueChanged(tag, newValue)) },
        isVisible = isVisible
    )
}

@Composable
private fun Tags(
    tags: List<Tag>,
    addNewFun: (focusOnNew: Boolean) -> Unit,
    onTitleChanged: (Tag, String) -> Unit,
    isVisible: Boolean
) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.padding(vertical = 20.dp)) {
        Label(stringResource(id = R.string.tags))
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

@Preview
@Composable
private fun Tags_Preview() {
    MaterialTheme {
        Surface {
            Tags(
                tags = listOf(Tag(value = "work"), Tag(value = "ui")),
                addNewFun = {},
                onTitleChanged = { _, _ -> },
                isVisible = true
            )
        }
    }
}