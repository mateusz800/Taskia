package com.mabn.taskia.domain.network.google.tasks

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface GoogleTasksApiClient {
    @GET("users/@me/lists")
    suspend fun getTaskLists(
        @Header("Authorization") auth: String,
    ): Response<GoogleTaskListsResponse>

    @GET("lists/{taskListId}/tasks")
    suspend fun getTasks(
        @Header("Authorization") auth: String,
        @Path("taskListId") taskListId: String,
        @Query("showCompleted") showCompleted: Boolean = false
    ): Response<GoogleTasksResponse>

    @PUT("lists/{taskListId}/tasks/{taskId}")
    suspend fun updateTask(
        @Header("Authorization") auth: String,
        @Path("taskListId") taskListId: String,
        @Path("taskId") taskId: String,
        @Body body: GoogleTaskUpdateDto
    ): Response<JsonObject>

    @DELETE("lists/{taskListId}/tasks/{taskId}")
    suspend fun deleteTask(
        @Header("Authorization") auth: String,
        @Path("taskListId") taskListId: String,
        @Path("taskId") taskId: String,
    ): Response<JsonObject>

    @POST("lists/{taskListId}/tasks")
    suspend fun insertTask(
        @Header("Authorization") auth: String,
        @Path("taskListId") taskListId: String,
        @Body body: GoogleTaskPostDto
    ): Response<GoogleTask>
}