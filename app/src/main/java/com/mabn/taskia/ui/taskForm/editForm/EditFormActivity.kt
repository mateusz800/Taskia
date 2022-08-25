package com.mabn.taskia.ui.taskForm.editForm

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.ui.common.base.ActivityWithActionBar
import com.mabn.taskia.ui.taskForm.TaskFormViewModel
import com.mabn.taskia.ui.theme.TaskiaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditFormActivity : ActivityWithActionBar() {
    private val viewModel: TaskFormViewModel by lazy { ViewModelProvider(this)[TaskFormViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val task: Task? = intent.extras?.get("task") as Task?
        if(task != null){
            setTask(task)
        }
        actionBar?.title = getString(R.string.emptyString)
        setContent {
            TaskiaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    EditFormView(viewModel = viewModel) {
                        this.finish()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }


    override fun onNavigateUp(): Boolean {
        if (viewModel.formState.value?.dataChanged != true) {
            finish()
            return true
        }
        return false
    }

    fun setTask(task:Task){
        viewModel.setTask(task, this)
    }
}