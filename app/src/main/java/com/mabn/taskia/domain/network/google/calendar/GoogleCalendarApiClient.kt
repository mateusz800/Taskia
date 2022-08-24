package com.mabn.taskia.domain.network.google.calendar

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

@Suppress("UNUSED")
interface GoogleCalendarApiClient {
    @GET("users/me/calendarList")
    suspend fun getCalendarsList(
        @Header("Authorization") auth: String,
    ): Response<GoogleCalendarListResponse>

    @GET("calendars/{calendarId}/events")
    suspend fun getCalendarEvents(
        @Header("Authorization") auth: String,
        @Path("calendarId") calendarId: String
    )
}