package com.mabn.taskia.ui.common

import android.content.Context
import android.content.Intent
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.ui.taskForm.editForm.EditFormActivity

fun startEditFormActivity(context: Context, task: Task) {
    val intent = Intent(
        context,
        EditFormActivity::class.java
    )
    intent.putExtra("task", task)
    context.startActivity(intent)
}