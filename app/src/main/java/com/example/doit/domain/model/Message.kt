package com.example.doit.domain.model

enum class MessageType{
    TOAST, SNACKBAR
}

data class Message(
    val text: String,
    val actionText: String? = null,
    val actionFun: () -> Unit = {},
    val type: MessageType = MessageType.TOAST
)
