package com.example.doit.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doit.R
import com.example.doit.ui.common.optionsDropdownMenu.OptionsDropdownMenu

@Composable
fun TopBar(onMenuClick: () -> Unit) {
    val menuExpanded = remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = stringResource(id = R.string.menu)
                )
            }
            IconButton(onClick = { menuExpanded.value = !menuExpanded.value }) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = stringResource(id = R.string.menu_more)
                )
            }
        }

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
        TopBar { }
    }
}