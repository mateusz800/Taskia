package com.mabn.taskia.domain.network.google.tasks

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleTasksApiClient {
    @GET("users/@me/lists")
    suspend fun getTaskLists(
        @Header("Authorization") auth: String,
    ): Response<GoogleTaskListsResponse>

    @GET("lists/{taskListId}/tasks")
    suspend fun getTasks(
        @Header("Authorization") auth: String,
        @Path("taskListId") taskListId: String,
        @Query("showCompleted") showCompleted:Boolean = true
    ): Response<GoogleTasksResponse>
}