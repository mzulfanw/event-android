package com.example.event_dicoding.data.repository

import com.example.event_dicoding.data.remote.response.EventItem
import com.example.event_dicoding.data.remote.retrofit.ApiService
import com.example.event_dicoding.domain.model.Event
import com.example.event_dicoding.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EventRepositoryImpl(private val apiService: ApiService) : EventRepository {
    
    private fun mapResponseToDomain(it: EventItem): Event {
        return Event(
            id = it.id,
            name = it.name,
            summary = it.summary,
            description = it.description,
            imageLogo = it.imageLogo,
            mediaCover = it.mediaCover,
            category = it.category,
            ownerName = it.ownerName,
            cityName = it.cityName,
            quota = it.quota,
            registrants = it.registrants,
            beginTime = it.beginTime,
            endTime = it.endTime,
            link = it.link
        )
    }

    override fun getActiveEvents(): Flow<List<Event>> = flow {
        val response = apiService.getEvents(active = 1)
        emit(response.listEvents.map { mapResponseToDomain(it) })
    }

    override fun getFinishedEvents(): Flow<List<Event>> = flow {
        val response = apiService.getEvents(active = 0)
        emit(response.listEvents.map { mapResponseToDomain(it) })
    }

    override fun searchEvents(query: String): Flow<List<Event>> = flow {
        val response = apiService.getEvents(active = -1, query = query)
        emit(response.listEvents.map { mapResponseToDomain(it) })
    }

    override fun getEventDetail(id: Int): Flow<Event> = flow {
        val response = apiService.getEventDetail(id)
        emit(mapResponseToDomain(response.event))
    }
}
