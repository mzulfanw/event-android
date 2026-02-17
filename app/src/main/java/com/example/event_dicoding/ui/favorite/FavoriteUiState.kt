package com.example.event_dicoding.ui.favorite

import com.example.event_dicoding.domain.model.Event

data class FavoriteUiState(
    val isLoading: Boolean = false,
    val favoriteEvents: List<Event> = emptyList(),
    val errorMessage: String? = null
)
