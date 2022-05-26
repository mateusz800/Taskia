package com.example.doit.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doit.R

@Composable
fun Drawer() {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(30.dp)
    ) {
        Icon(
            Icons.Filled.Share,
            contentDescription = stringResource(id = R.string.share_app)
        )
        Spacer(Modifier.width(10.dp))
        Text(stringResource(id = R.string.share_app))
    }
}

@Preview
@Composable
private fun Drawer_Preview(){
    MaterialTheme{
        Drawer()
    }
}