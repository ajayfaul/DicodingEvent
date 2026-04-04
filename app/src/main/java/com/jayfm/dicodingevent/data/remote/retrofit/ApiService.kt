package com.jayfm.dicodingevent.data.remote.retrofit

import com.jayfm.dicodingevent.data.remote.response.DetailEventResponse
import com.jayfm.dicodingevent.data.remote.response.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getEvents(
        @Query("active") active: Int,
        @Query("q") query: String? = null,
        @Query("limit") limit: Int? = null
    ): EventResponse

    @GET("events/{id}")
    suspend fun getDetailEvent(
        @Path("id") id: String
    ): DetailEventResponse
}
