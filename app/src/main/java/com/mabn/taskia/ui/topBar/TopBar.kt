package com.mabn.taskia.ui.topBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.ui.common.Tabs
import com.mabn.taskia.ui.common.optionsDropdownMenu.OptionsDropdownMenu
import com.mabn.taskia.ui.taskList.TaskListViewModel
import com.mabn.taskia.ui.topBar.filterDropdownMenu.FilterDropDown

@Composable
fun TopBar(
    viewModel: TopBarViewModel,
    taskListViewModel: TaskListViewModel,
    tabs: List<Pair<String, () -> Unit>>
) {
    val state by viewModel.topBarState.observeAsState()

    TopBar(
        tabs = tabs,
        menuExpanded = state?.menuExpanded ?: false,
        filterExpanded = state?.filterMenuExpanded ?: false,
        selectedTabIndex = state?.tabIndex ?: 0,
        filterCount = taskListViewModel.filterTags.value?.size ?: 0,
        onEvent = viewModel::onEvent
    )

}

@Composable
private fun TopBar(
    tabs: List<Pair<String, () -> Unit>>,
    menuExpanded: Boolean,
    filterExpanded: Boolean,
    filterCount: Int,
    selectedTabIndex: Int,
    onEvent: (TopBarEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.height(50.dp),
                    tint = MaterialTheme.colors.onPrimary
                )
                Text(
                    stringResource(id = R.string.app_name),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onPrimary
                )
            }
            Row {
                Column(horizontalAlignment = Alignment.End) {
                    IconButton(onClick = { onEvent(TopBarEvent.ToggleFilterMenu()) }) {
                        Icon(
                            Icons.Filled.FilterList, null,
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                    BadgedBox(badge = {
                        if (filterCount > 0) {
                            Badge(
                                backgroundColor = MaterialTheme.colors.secondary,
                                contentColor = MaterialTheme.colors.onSecondary
                            ) {
                                Text(filterCount.toString())
                            }
                        }
                    }, modifier = Modifier.offset(x = (-10).dp, y = (-10).dp)) {

                    }
                }
                IconButton(onClick = { onEvent(TopBarEvent.ToggleMenu()) }) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = stringResource(id = R.string.menu_more),
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            Box {
                OptionsDropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { onEvent(TopBarEvent.ToggleMenu(forceDismiss = true)) })
                FilterDropDown(
                    expanded = filterExpanded
                ) {
                    onEvent(TopBarEvent.ToggleFilterMenu(forceDismiss = true))
                }
            }
        }

        Tabs(
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
            changeTab = { index -> onEvent(TopBarEvent.TabChanged(index)) })
    }
}

@Preview
@Composable
private fun TopBar_Preview() {
    MaterialTheme {
        TopBar(
            tabs = listOf(),
            menuExpanded = false,
            filterExpanded = false,
            filterCount = 0,
            selectedTabIndex = 0 ,
            onEvent = {}
        )
    }
}