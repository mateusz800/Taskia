package com.example.doit.domain.model

data class Message(
    val text: String,
    val actionText: String?,
    val actionFun: () -> Unit
)
