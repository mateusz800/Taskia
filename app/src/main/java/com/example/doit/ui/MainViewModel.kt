package com.example.doit.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor():ViewModel() {
    @OptIn(ExperimentalMaterialApi::class)
    private val _currentBottomSheetState = MutableStateFlow(ModalBottomSheetValue.Hidden)
    @OptIn(ExperimentalMaterialApi::class)
    val currentScreenState: StateFlow<ModalBottomSheetValue>
        get() = _currentBottomSheetState
}