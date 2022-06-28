package com.mabn.taskia.domain.network.google.tasks

data class GoogleTaskPostDto(
    val status:String,
    val title: String,
    val due:String
)