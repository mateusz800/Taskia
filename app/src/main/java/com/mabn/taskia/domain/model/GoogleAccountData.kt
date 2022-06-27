package com.mabn.taskia.domain.model



data class GoogleAccountData(
    val taskListsIdList:MutableList<String> = mutableListOf(),
    var eventsListSyncToken:String? = null
)
