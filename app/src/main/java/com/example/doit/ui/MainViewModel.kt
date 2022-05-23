package com.example.doit.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doit.domain.model.Message
import com.example.doit.domain.model.Task
import com.example.doit.domain.persistence.repository.MessageRepository
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
    private val _message = MutableLiveData<Message?>(null)
    val message: LiveData<Message?>
        get() = _message

    init {
        collectData()
    }

    fun clearMessage(){
        _message.postValue(null)
    }

    private fun collectData(){
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.getAll().collect {
               _message.postValue(it)
            }
        }
    }

}