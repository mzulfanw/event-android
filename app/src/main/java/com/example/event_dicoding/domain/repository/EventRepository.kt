package com.example.event_dicoding.domain.repository

import com.example.event_dicoding.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getActiveEvents(): Flow<List<Event>>
    fun getFinishedEvents(): Flow<List<Event>>
    fun searchEvents(query: String): Flow<List<Event>>
    fun getEventDetail(id: Int): Flow<Event>

    fun getAllFavoriteEvents(): Flow<List<Event>>
    fun isFavorite(id: Int): Flow<Boolean>
    suspend fun insertFavorite(event: Event)
    suspend fun deleteFavorite(event: Event)
}
