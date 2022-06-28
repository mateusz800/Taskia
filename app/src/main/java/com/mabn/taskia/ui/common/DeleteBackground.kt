package com.mabn.taskia.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R

@Composable
fun DeleteBackground() {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .fillMaxHeight()
            .clip(RectangleShape)
            .background(Color.Red),
        contentAlignment = Alignment.CenterStart
    ) {
        Icon(
            Icons.Outlined.Delete,
            contentDescription = stringResource(id = R.string.remove),
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}