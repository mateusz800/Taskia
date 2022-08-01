package com.mabn.taskia.ui.topBar.filterDropdownMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Tag
import com.mabn.taskia.ui.common.ButtonWithoutBackground
import com.mabn.taskia.ui.taskList.ListEvent
import com.mabn.taskia.ui.taskList.TaskListViewModel

@Composable
fun FilterDropDown(
    taskListViewModel: TaskListViewModel,
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    val viewModel: FilterDropdownMenuViewModel = hiltViewModel()
    val allTags = taskListViewModel.allTags.observeAsState()
    val tags = viewModel.tags.observeAsState()

    LaunchedEffect(allTags.value) {
        viewModel.setTags(allTags.value)
    }

    FilterDropDown(
        onEvent = viewModel::onEvent,
        onListEvent = taskListViewModel::onEvent,
        tags = tags.value ?: listOf(),
        expanded = expanded
    ) {
        onDismissRequest()
    }


}

@Composable
private fun FilterDropDown(
    onEvent: (FilterMenuEvent) -> Unit,
    onListEvent: (ListEvent) -> Unit,
    tags: List<Pair<Tag, Boolean>>,
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier.padding(
            horizontal = 10.dp
        )
    ) {
        if (tags.isNullOrEmpty()) {
            Text(stringResource(id = R.string.no_tags))
        } else {
            Box(contentAlignment = Alignment.TopEnd, modifier = Modifier.fillMaxWidth()) {
                ButtonWithoutBackground(onClick = {
                    onEvent(FilterMenuEvent.TagsCleared)
                    onListEvent(ListEvent.FilterTagsChanged(listOf()))
                    onDismissRequest()
                }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Clear, null)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(stringResource(id = R.string.clear_filters))
                    }

                }
            }

            tags.forEach { tag ->
                DropdownMenuItem(
                    onClick = {
                        onEvent(FilterMenuEvent.TagSelected(tag.first))
                        onListEvent(ListEvent.FilterTagsChanged(
                            tags.filter { pair -> pair.second }
                                .map { pair -> pair.first }
                        ))
                    },
                    modifier = Modifier.background(if (tag.second) MaterialTheme.colors.primary else Color.Transparent)
                ) {
                    Text(tag.first.value)
                }
            }
        }
    }
}


@Preview
@Composable
private fun FilterDropdownMenu_Preview() {
    MaterialTheme {
        Surface {
            FilterDropDown(
                onEvent = {},
                onListEvent = {},
                tags = listOf(Pair(Tag(value = "work"), true), Pair(Tag(value = "home"), false)),
                expanded = true
            ) {

            }
        }
    }
}