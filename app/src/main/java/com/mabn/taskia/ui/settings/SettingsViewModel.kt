package com.mabn.taskia.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _changeActivityEvent: MutableLiveData<SettingsEvent.ActivityChanged> =
        MutableLiveData()
    val changeActivityEvent: LiveData<SettingsEvent.ActivityChanged> = _changeActivityEvent


    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ActivityChanged -> _changeActivityEvent.postValue(event)
        }
    }
}