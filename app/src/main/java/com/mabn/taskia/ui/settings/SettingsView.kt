package com.mabn.taskia.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mabn.taskia.BuildConfig
import com.mabn.taskia.R
import com.mabn.taskia.ui.theme.TaskiaTheme

@Composable
fun SettingsView(viewModel: SettingsViewModel) {
    TaskiaTheme {
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


