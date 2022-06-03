package com.mabn.taskia.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.ui.common.optionsDropdownMenu.OptionsDropdownMenu

@Composable
fun TopBar(tabs: List<Pair<String, () -> Unit>>, onMenuClick: () -> Unit) {
    val menuExpanded = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
    ) {

        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = stringResource(id = R.string.menu),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
            /*
            IconButton(onClick = { menuExpanded.value = !menuExpanded.value }) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = stringResource(id = R.string.menu_more)
                )
            }

             */
        }
        Tabs(tabs = tabs)

        Box(
            modifier = Modifier
                .padding(end = 10.dp)
        ) {
            OptionsDropdownMenu(
                expanded = menuExpanded.value,
                onDismissRequest = { menuExpanded.value = false })
        }
    }
}


@Preview
@Composable
private fun TopBar_Preview() {
    MaterialTheme {
        TopBar(tabs = listOf()) { }
    }
}