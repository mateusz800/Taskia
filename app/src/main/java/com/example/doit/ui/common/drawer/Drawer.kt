package com.example.doit.ui.common.drawer

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.doit.R

@Composable
fun Drawer(content: @Composable () -> Unit) {
    val viewModel: DrawerViewModel = hiltViewModel()
    Column(modifier = Modifier) {
        content()
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            ButtonsBar(viewModel = viewModel)
        }

    }
}

@Composable
private fun ButtonsBar(viewModel: DrawerViewModel) {
    Column {
        Divider()
        Row(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            SettingsButton {

            }
            ShareAppButton {
                viewModel.shareApp()
            }
        }
    }
}

@Composable
private fun BottomBarButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String? = null
) {
    IconButton(onClick = onClick) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = Color.Black
        )
    }
}

@Composable
private fun SettingsButton(onClick: () -> Unit) {
    BottomBarButton(
        onClick = onClick,
        icon = Icons.Outlined.Settings,
        contentDescription = stringResource(id = R.string.settings)
    )
}

@Composable
private fun ShareAppButton(onClick: () -> Unit) {
    BottomBarButton(
        onClick = onClick,
        icon = Icons.Filled.Share,
        contentDescription = stringResource(id = R.string.share_app)
    )
}


@Preview
@Composable
private fun Drawer_Preview() {
    MaterialTheme {
        Drawer {
            Text("Today")
        }
    }
}