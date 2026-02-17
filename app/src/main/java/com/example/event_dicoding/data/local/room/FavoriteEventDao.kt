package com.example.event_dicoding.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteEvent: FavoriteEvent)

    @Delete
    suspend fun delete(favoriteEvent: FavoriteEvent)

    @Query("SELECT * FROM favorite_event ORDER BY id ASC")
    fun getAllFavoriteEvents(): Flow<List<FavoriteEvent>>

    @Query("SELECT EXISTS(SELECT * FROM favorite_event WHERE id = :id)")
    fun isFavorite(id: Int): Flow<Boolean>
}
