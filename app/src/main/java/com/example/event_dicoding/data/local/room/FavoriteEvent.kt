package com.example.event_dicoding.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_event")
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val mediaCover: String,
    val cityName: String,
    val beginTime: String
)
