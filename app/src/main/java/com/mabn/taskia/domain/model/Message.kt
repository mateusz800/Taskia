package com.mabn.taskia.domain.model

enum class MessageType{
    TOAST, SNACKBAR, LOADED_EVENT
}

data class Message(
    val text: String,
    val actionText: String? = null,
    val actionFun: () -> Unit = {},
    val type: MessageType = MessageType.TOAST
)
