package com.mabn.taskia.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mabn.taskia.R
import com.mabn.taskia.ui.common.optionsDropdownMenu.OptionsDropdownMenu
import com.mabn.taskia.ui.taskList.TaskListViewModel
import com.mabn.taskia.ui.taskList.view.filterDropdownMenu.FilterDropDown

@Composable
fun TopBar(tabs: List<Pair<String, () -> Unit>>, onMenuClick: () -> Unit) {
    val menuExpanded = remember { mutableStateOf(false) }
    val filterExpanded = remember { mutableStateOf(false) }
    val taskListViewModel: TaskListViewModel = hiltViewModel()
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
            /*IconButton(onClick = onMenuClick) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = stringResource(id = R.string.menu),
                    tint = MaterialTheme.colors.onPrimary
                )
            }*/

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
                    IconButton(onClick = { filterExpanded.value = !filterExpanded.value }) {
                        Icon(
                            Icons.Filled.FilterList, null,
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                    BadgedBox(badge = {
                        val count = taskListViewModel.filterTags.value?.size
                        if (count != null && count > 0) {
                            Badge(
                                backgroundColor = MaterialTheme.colors.secondary,
                                contentColor = MaterialTheme.colors.onSecondary
                            ) {
                                Text(count.toString())
                            }
                        }
                    }, modifier = Modifier.offset(x = (-10).dp, y = (-10).dp)) {

                    }
                }



                IconButton(onClick = { menuExpanded.value = !menuExpanded.value }) {
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
                    expanded = menuExpanded.value,
                    onDismissRequest = { menuExpanded.value = false })
                FilterDropDown(
                    taskListViewModel = taskListViewModel,
                    expanded = filterExpanded.value
                ) {
                    filterExpanded.value = false
                }
            }
        }

        Tabs(tabs = tabs)
    }


}


@Preview
@Composable
private fun TopBar_Preview() {
    MaterialTheme {
        TopBar(tabs = listOf()) { }
    }
}