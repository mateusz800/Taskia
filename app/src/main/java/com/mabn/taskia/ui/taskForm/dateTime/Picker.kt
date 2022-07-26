package com.mabn.taskia.ui.taskForm.dateTime

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mabn.taskia.ui.common.CustomTextField

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Picker(
    label: String,
    icon: (@Composable () -> Unit),
    menuItem: Map<String, () -> Unit>
) {
    val menuExpanded = remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = menuExpanded.value,
        modifier = Modifier
            .width(200.dp),
        onExpandedChange = { menuExpanded.value = !menuExpanded.value }) {

        CustomTextField(
            leadingIcon = icon,
            enabled = false,
            value = label,
            onValueChange = {},
            modifier = Modifier
                .background(Color.Transparent)
                .padding(horizontal = 10.dp)
                //.border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp))
                .height(30.dp)
                .widthIn(min = 200.dp)
                .focusable(false)
        )

        ExposedDropdownMenu(
            modifier = Modifier
                .padding(0.dp)
                .focusable(true),
            expanded = menuExpanded.value,
            onDismissRequest = { menuExpanded.value = false }) {
            menuItem.entries.forEach {
                DropdownMenuItem(onClick = {
                    it.value.invoke()
                    menuExpanded.value = false
                }) {
                    Text(it.key)
                }
            }
        }
    }
}