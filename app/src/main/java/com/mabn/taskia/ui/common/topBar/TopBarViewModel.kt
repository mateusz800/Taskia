package com.mabn.taskia.ui.common.topBar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopBarViewModel @Inject constructor() : ViewModel() {
    private val _tabIndex = MutableLiveData<Int>(0)
    val tabIndex: LiveData<Int> = _tabIndex

    fun changeTab(index:Int){
        _tabIndex.postValue(index)
    }
}