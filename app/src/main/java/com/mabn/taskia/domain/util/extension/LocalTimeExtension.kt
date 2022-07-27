package com.mabn.taskia.domain.util.extension

import android.content.Context
import com.mabn.taskia.R
import java.time.LocalTime

fun LocalTime?.toFormattedString(context: Context): String {
    return if (this != null)
        "%02d".format(this.hour) + ":" + "%02d".format(this.minute)
    else context.getString(R.string.no_time)
}