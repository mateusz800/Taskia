package com.mabn.taskia.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mabn.taskia.domain.model.Message
import com.mabn.taskia.domain.model.MessageType
import com.mabn.taskia.domain.network.TasksSynchronizer
import com.mabn.taskia.domain.persistence.repository.MessageRepository
import com.mabn.taskia.domain.util.ContextProvider
import com.mabn.taskia.domain.util.IntentAction
import com.mabn.taskia.domain.util.KeyboardHeightProvider
import com.mabn.taskia.ui.taskList.ListType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val contextProvider: ContextProvider,
    private val tasksSynchronizer: TasksSynchronizer
) : ViewModel(), KeyboardHeightProvider.KeyboardHeightListener {
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                IntentAction.ACTION_APP_LOADED -> {
                    viewModelScope.launch(Dispatchers.IO) { _currentState.emit(MainViewState.Loaded) }
                }
            }
        }
    }

    private val _currentState = MutableStateFlow<MainViewState>(MainViewState.Loading)
    val currentState: StateFlow<MainViewState>
        get() = _currentState

    private val _keyboardHeight = MutableLiveData<Int>()
    val keyboardHeight: LiveData<Int> = _keyboardHeight

    private val _isLandscape = MutableLiveData<Boolean>()
    val isLandscape: LiveData<Boolean> = _isLandscape

    init {
        val filter = IntentFilter().apply {
            addAction(IntentAction.ACTION_APP_LOADED)
        }
        contextProvider.getContext().registerReceiver(broadcastReceiver, filter)
    }


    val availableListSet = setOf(
        ListType.Today,
        ListType.Upcoming,
        ListType.Unscheduled,
        ListType.Completed
    )
    private val _currentList = MutableStateFlow<ListType>(ListType.Today)
    val currentList: StateFlow<ListType> = _currentList

    private val _message = MutableLiveData<Message?>(null)
    val message: LiveData<Message?>
        get() = _message

    init {
        collectData()
    }

    fun clearMessage() {
        _message.postValue(null)
    }

    fun showList(listType: ListType) {
        _currentList.value = listType
    }

    private fun collectData() {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.getAll().collect {
                if (it.type == MessageType.LOADED_EVENT) {
                    _currentState.emit(MainViewState.Loaded)
                }
                _message.postValue(it)
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            tasksSynchronizer.sync()
        }
    }

    override fun onKeyboardHeightChanged(height: Int, isLandscape: Boolean) {
        _keyboardHeight.postValue(height)
        _isLandscape.postValue(isLandscape)
    }

}