package com.example.event_dicoding.domain.usecase

import com.example.event_dicoding.domain.model.Event
import com.example.event_dicoding.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow

class GetActiveEventsUseCase(private val repository: EventRepository) {
    operator fun invoke(): Flow<List<Event>> = repository.getActiveEvents()
}

class GetFinishedEventsUseCase(private val repository: EventRepository) {
    operator fun invoke(): Flow<List<Event>> = repository.getFinishedEvents()
}

class SearchEventsUseCase(private val repository: EventRepository) {
    operator fun invoke(query: String): Flow<List<Event>> = repository.searchEvents(query)
}

class GetEventDetailUseCase(private val repository: EventRepository) {
    operator fun invoke(id: Int): Flow<Event> = repository.getEventDetail(id)
}
