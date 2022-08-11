package com.mabn.taskia.domain.util

class ViewModelsCommunicationBridge<T> {
    private var _onMessageReceived: ((T) -> Unit)? = null
    fun registerCallback(onMessageReceived: (T) -> Unit) {
        this._onMessageReceived = onMessageReceived
    }

    fun onDispatchMessage(message: T) {
        _onMessageReceived?.invoke(message)
    }

    fun unregister() {
        _onMessageReceived = null
    }
}