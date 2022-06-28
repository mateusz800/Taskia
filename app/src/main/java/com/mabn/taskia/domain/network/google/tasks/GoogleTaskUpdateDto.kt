package com.mabn.taskia.domain.network.google.tasks

data class GoogleTaskUpdateDto(
    val status:String,
    val id: String,
    val title: String,
    val due:String
)