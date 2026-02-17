package com.example.event_dicoding.data.remote.retrofit

import com.example.event_dicoding.data.remote.response.DetailResponse
import com.example.event_dicoding.data.remote.response.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getEvents(
        @Query("active") active: Int = -1,
        @Query("q") query: String? = null,
        @Query("limit") limit: Int? = null
    ): EventResponse

    @GET("events/{id}")
    suspend fun getEventDetail(
        @Path("id") id: Int
    ): DetailResponse
}
