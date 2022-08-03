package com.mabn.taskia.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mabn.taskia.ui.topBar.TopBarViewModel

@Composable
fun Tabs(
    tabs: List<Pair<String, () -> Unit>>,
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
                Text(
                    tab.first,
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }
        }
    }
}
