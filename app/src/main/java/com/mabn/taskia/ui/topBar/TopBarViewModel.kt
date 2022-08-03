package com.mabn.taskia.ui.topBar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopBarViewModel @Inject constructor() : ViewModel() {
    private val _topBarState = MutableLiveData(TopBarState())
    val topBarState: LiveData<TopBarState> = _topBarState

    fun onEvent(event: TopBarEvent) {
        when (event) {
            is TopBarEvent.ToggleFilterMenu -> _topBarState.postValue(
                _topBarState.value?.copy(
                    filterMenuExpanded = if (event.forceDismiss) false
                    else !(_topBarState.value?.filterMenuExpanded ?: true)
                )
            )
            is TopBarEvent.ToggleMenu -> _topBarState.postValue(
                _topBarState.value?.copy(
                    menuExpanded = if (event.forceDismiss) false
                    else !(_topBarState.value?.menuExpanded ?: true)
                )
            )
            is TopBarEvent.TabChanged -> _topBarState.postValue(
                _topBarState.value?.copy(
                    tabIndex = event.index
                )
            )
        }
    }
}