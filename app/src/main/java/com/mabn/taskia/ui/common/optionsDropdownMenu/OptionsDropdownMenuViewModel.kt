package com.mabn.taskia.ui.common.optionsDropdownMenu

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mabn.taskia.domain.network.TasksSynchronizer
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import com.mabn.taskia.domain.util.ContextProvider
import com.mabn.taskia.ui.settings.SettingsActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OptionsDropdownMenuViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val contextProvider: ContextProvider,
    private val tasksSynchronizer: TasksSynchronizer
) : ViewModel() {

    fun startSettingsActivity() {
        val context = contextProvider.getContext()
        val intent = Intent(context, SettingsActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun syncTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            tasksSynchronizer.sync()
        }
    }
}