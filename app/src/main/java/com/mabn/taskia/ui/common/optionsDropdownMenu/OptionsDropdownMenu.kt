package com.mabn.taskia.ui.common.optionsDropdownMenu

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mabn.taskia.R

@Composable
fun OptionsDropdownMenu(expanded: Boolean, onDismissRequest: () -> Unit) {
    val viewModel: OptionsDropdownMenuViewModel = hiltViewModel()
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            onClick = {
                viewModel.startSettingsActivity()
            }) {
            Text(stringResource(id = R.string.settings))
        }
    }
}


@Preview
@Composable
private fun OptionsDropdownMenu_Preview() {
    MaterialTheme {
        OptionsDropdownMenu(expanded = true) {

        }
    }
}