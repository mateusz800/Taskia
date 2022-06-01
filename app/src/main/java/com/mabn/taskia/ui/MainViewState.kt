package com.mabn.taskia.ui

sealed class MainViewState {
    object Loading: MainViewState()
    object Loaded: MainViewState()
}