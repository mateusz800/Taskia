package com.mabn.taskia.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mabn.taskia.BuildConfig
import com.mabn.taskia.R
import com.mabn.taskia.ui.theme.DoItTheme

@Composable
fun SettingsView(viewModel: SettingsViewModel) {
    DoItTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            SettingsView(viewModel::onEvent)
        }
    }
}

@Composable
private fun SettingsView(onEvent: (SettingsEvent) -> Unit) {
    Column(Modifier.padding(top=10.dp)) {
        Column {
            SettingsSectionLabel(text = stringResource(id = R.string.customize))
            SettingsButton(text = stringResource(id = R.string.connected_accounts), onClick = {
                onEvent(SettingsEvent.ActivityChanged(SettingsActivityEnum.CONNECTED_ACCOUNT))
            })
        }
        Column {
            SettingsSectionLabel(text = stringResource(id = R.string.about))
            SettingsButton(
                text = stringResource(id = R.string.version) + ": " + BuildConfig.VERSION_NAME,
                onClick = { })
        }
    }
}


@Preview
@Composable
private fun SettingsView_Preview() {
    MaterialTheme {
        Surface {
            SettingsView(onEvent = {})
        }
    }
}


