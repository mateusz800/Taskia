package com.mabn.taskia.ui.common.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mabn.taskia.R

abstract class ActivityWithActionBar:ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        actionBar?.setDisplayHomeAsUpEnabled( true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        actionBar?.setDisplayShowTitleEnabled(true)
        super.onCreate(savedInstanceState)
    }
}