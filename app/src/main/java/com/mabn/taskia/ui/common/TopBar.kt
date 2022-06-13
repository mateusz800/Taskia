package com.mabn.taskia.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
            /*
            IconButton(onClick = { menuExpanded.value = !menuExpanded.value }) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = stringResource(id = R.string.menu_more),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
             */

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