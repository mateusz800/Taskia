package com.mabn.taskia.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Tabs(
    tabs: List<Pair<Pair<String, ImageVector?>, () -> Unit>>,
    selectedTabIndex: Int = 0,
    changeTab: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    ) {
        tabs.forEachIndexed { index, tab ->
            val isSelected = selectedTabIndex == index
            Tab(
                selected = isSelected,
                selectedContentColor = MaterialTheme.colors.onPrimary,
                unselectedContentColor = MaterialTheme.colors.onPrimary,
                onClick = {
                    tab.second.invoke()
                    changeTab(index)
                }, modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Row {
                    if (tab.first.second != null) {
                        Icon(
                            tab.first.second!!,
                            tab.first.first,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    }
                    Text(
                        tab.first.first,
                        color = MaterialTheme.colors.onPrimary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

            }
        }
    }
}
