package com.example.event_dicoding.ui.detail

import com.example.event_dicoding.domain.model.Event

data class DetailUiState(
    val isLoading: Boolean = false,
    val event: Event? = null,
    val errorMessage: String? = null
)
