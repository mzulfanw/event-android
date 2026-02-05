package com.example.event_dicoding.ui.home

import com.example.event_dicoding.domain.model.Event

data class HomeUiState(
    val isLoading: Boolean = false,
    val activeEvents: List<Event> = emptyList(),
    val finishedEvents: List<Event> = emptyList(),
    val searchResults: List<Event> = emptyList(),
    val isSearching: Boolean = false,
    val errorMessage: String? = null
)
