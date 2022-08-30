package com.mabn.taskia.ui.taskForm.components.dateTime

import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS
import android.widget.TimePicker


class CustomTimePicker(
    context: Context,
    listener: TimePickerDialog.OnTimeSetListener,
    hourOfDay: Int,
    minute: Int,
    is24HourView: Boolean
) : TimePickerDialog(context, listener, hourOfDay, minute, is24HourView) {

    init {
        lateinit var mTimePicker: TimePicker
        TimePickerDialog::class.java.getDeclaredField("mTimePicker").let {
            it.isAccessible = true
            mTimePicker = it.get(this) as TimePicker
        }
        mTimePicker.descendantFocusability = FOCUS_BLOCK_DESCENDANTS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mTimePicker.focusable = View.NOT_FOCUSABLE
        }
        //window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

    }
}