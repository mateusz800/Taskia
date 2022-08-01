package com.mabn.taskia.ui.taskList.components.taskItem

import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ExpandButton(expand: Boolean, toggle: () -> Unit) {
    IconButton(
        modifier = Modifier.requiredWidth(20.dp),
        onClick = toggle
    ) {
        Icon(
            if (expand) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
            null
        )
    }
}

@Preview
@Composable
private fun ExpandButton_Expand_Preview() {
    MaterialTheme {
        Surface {
            ExpandButton(expand = true) {

            }
        }
    }
}

@Preview
@Composable
private fun ExpandButton_Hide_Preview() {
    MaterialTheme {
        Surface {
            ExpandButton(expand = false) {

            }
        }
    }
}

