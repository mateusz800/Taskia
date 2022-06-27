package com.mabn.taskia.domain.network.google.tasks

data class GoogleTasksResponse(
    val items: List<GoogleTask>
)

data class GoogleTask(
    val id: String,
    val title: String,
    val status: String,
    val due: String?,
)
