package com.example.event_dicoding.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("event")
    val event: EventItem
)
