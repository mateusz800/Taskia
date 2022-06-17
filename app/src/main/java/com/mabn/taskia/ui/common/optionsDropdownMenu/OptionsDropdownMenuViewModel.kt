package com.mabn.taskia.ui.common.optionsDropdownMenu

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.lifecycle.ViewModel
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import com.mabn.taskia.domain.util.ContextProvider
import com.mabn.taskia.ui.settings.SettingsActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OptionsDropdownMenuViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val contextProvider: ContextProvider
) : ViewModel() {

    fun startSettingsActivity(){
        val context = contextProvider.getContext()
        val intent = Intent(context, SettingsActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)

    }
}