package com.example.doit.ui

sealed class MainViewState {
    object Loading: MainViewState()
    object Loaded: MainViewState()
}