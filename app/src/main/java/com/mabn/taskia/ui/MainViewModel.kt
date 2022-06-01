package com.mabn.taskia.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mabn.taskia.domain.model.Message
import com.mabn.taskia.domain.model.MessageType
import com.mabn.taskia.domain.persistence.repository.MessageRepository
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
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val _currentState = MutableStateFlow<MainViewState>(MainViewState.Loading)
    val currentState: StateFlow<MainViewState>
        get() = _currentState


    val availableListSet = setOf(
        ListType.Today,
        ListType.Unscheduled,
        ListType.Upcoming,
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

}