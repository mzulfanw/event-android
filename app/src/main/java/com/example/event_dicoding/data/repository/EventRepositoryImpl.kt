package com.example.event_dicoding.data.repository

import com.example.event_dicoding.data.local.room.FavoriteEventDao
import com.example.event_dicoding.data.local.room.FavoriteEvent as FavoriteEventEntity
import com.example.event_dicoding.data.remote.response.EventItem
import com.example.event_dicoding.data.remote.retrofit.ApiService
import com.example.event_dicoding.domain.model.Event
import com.example.event_dicoding.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class EventRepositoryImpl(
    private val apiService: ApiService,
    private val favoriteEventDao: FavoriteEventDao
) : EventRepository {
    
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

    private fun mapFavoriteToDomain(it: FavoriteEventEntity): Event {
        return Event(
            id = it.id,
            name = it.name,
            summary = "",
            description = "",
            imageLogo = "",
            mediaCover = it.mediaCover,
            category = "",
            ownerName = "",
            cityName = it.cityName,
            quota = 0,
            registrants = 0,
            beginTime = it.beginTime,
            endTime = "",
            link = ""
        )
    }

    private fun mapDomainToFavorite(it: Event): FavoriteEventEntity {
        return FavoriteEventEntity(
            id = it.id,
            name = it.name,
            mediaCover = it.mediaCover,
            cityName = it.cityName,
            beginTime = it.beginTime
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

    override fun getAllFavoriteEvents(): Flow<List<Event>> {
        return favoriteEventDao.getAllFavoriteEvents().map { list ->
            list.map { mapFavoriteToDomain(it) }
        }
    }

    override fun isFavorite(id: Int): Flow<Boolean> = favoriteEventDao.isFavorite(id)

    override suspend fun insertFavorite(event: Event) {
        favoriteEventDao.insert(mapDomainToFavorite(event))
    }

    override suspend fun deleteFavorite(event: Event) {
        favoriteEventDao.delete(mapDomainToFavorite(event))
    }
}
