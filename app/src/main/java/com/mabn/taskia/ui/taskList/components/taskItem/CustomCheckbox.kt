package com.mabn.taskia.ui.taskList.components.taskItem

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomCheckbox(status: Boolean, enabled: Boolean = true, onCheck: () -> Boolean) {
    val checked = remember { mutableStateOf(status) }
    Box(
        modifier = Modifier
            .height(40.dp)
            .width(30.dp)
            .padding(vertical = 10.dp)
            .padding(end = 10.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onBackground,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(enabled = enabled) {
                if (onCheck()) {
                    checked.value = !checked.value
                }
            }
    ) {
        if (checked.value) {
            Icon(
                Icons.Default.Check, status.toString(),
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@Preview
@Composable
private fun CustomCheckbox_Preview() {
    MaterialTheme {
        Surface {
            CustomCheckbox(status = false) {
                true
            }
        }
    }
}

@Preview
@Composable
private fun CustomCheckbox_Checked_Preview() {
    MaterialTheme {
        Surface {
            CustomCheckbox(status = true) {
                true
            }
        }
    }
}