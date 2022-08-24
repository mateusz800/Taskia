package com.mabn.taskia.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.mabn.taskia.R


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = stringResource(id = R.string.new_subtask),
    style: TextStyle = MaterialTheme.typography.body2,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    underline: Boolean = false,
    alignCenter: Boolean = false
) {
    var text by rememberSaveable { mutableStateOf(value) }
    BasicTextField(
        enabled = enabled,
        modifier = modifier
            .background(
                Color.Transparent,
                MaterialTheme.shapes.small,
            )
            .width(IntrinsicSize.Min),

        value = value,
        onValueChange = {
            text = it.replace("\n", "")
            onValueChange(text)
        },
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        textStyle = style.copy(
            color = MaterialTheme.colors.onBackground,
            textAlign = if (alignCenter) TextAlign.Center else TextAlign.Start
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            Row(
                modifier.drawBehind {
                    if (underline) {
                        val strokeWidth = 1 * density
                        val y = size.height + 5 * density + strokeWidth / 2

                        drawLine(
                            Color.LightGray,
                            Offset(0f, y),
                            Offset(size.width, y),
                            strokeWidth
                        )
                    }
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()
                Box {
                    if (value.isEmpty()) Text(
                        placeholderText,
                        style = style.copy(
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                        )
                    )
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        }
    )
}



