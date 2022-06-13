package com.mabn.taskia.ui.common.optionsDropdownMenu

import android.content.Intent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mabn.taskia.R
import com.mabn.taskia.ui.settings.SettingsActivity

@Composable
fun OptionsDropdownMenu(expanded: Boolean, onDismissRequest: () -> Unit) {
    val viewModel: OptionsDropdownMenViewModel = hiltViewModel()
    val context = LocalContext.current
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            onClick = {
                context.startActivity(Intent(context, SettingsActivity::class.java))
            }) {
            Text(stringResource(id = R.string.settings))
        }
        Divider()
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