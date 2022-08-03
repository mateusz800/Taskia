package com.mabn.taskia.domain.util.extension

import android.content.res.Resources.getSystem

val Int.toDp: Int get() = (this / getSystem().displayMetrics.density).toInt()