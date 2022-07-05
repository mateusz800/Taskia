package com.mabn.taskia.ui.taskList.view.filterDropdownMenu

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mabn.taskia.R
import com.mabn.taskia.ui.common.ButtonWithoutBackground
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


    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier.padding(
            horizontal = 10.dp
        )
    ) {
        if (tags.value.isNullOrEmpty()) {
            Text(stringResource(id = R.string.no_tags))
        } else {
            Box(contentAlignment = Alignment.TopEnd, modifier = Modifier.fillMaxWidth()) {
                ButtonWithoutBackground(onClick = {
                    taskListViewModel.setFilterTags(listOf())
                    viewModel.clearSelectedTags()
                    onDismissRequest()
                }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Clear, null)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(stringResource(id = R.string.clear_filters))
                    }

                }
            }

            tags.value?.forEach {
                DropdownMenuItem(
                    onClick = {
                        viewModel.selectTag(it.first)
                        taskListViewModel.setFilterTags(viewModel.getSelectedTags())
                    },
                    modifier = Modifier.background(if (it.second) MaterialTheme.colors.primary else Color.Transparent)
                ) {
                    Text(it.first.value)
                }
            }
        }
    }
}