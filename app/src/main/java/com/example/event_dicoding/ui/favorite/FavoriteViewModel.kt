package com.example.event_dicoding.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.event_dicoding.domain.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: EventRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()

    init {
        getFavoriteEvents()
    }

    private fun getFavoriteEvents() {
        viewModelScope.launch {
            repository.getAllFavoriteEvents()
                .onStart { _uiState.value = _uiState.value.copy(isLoading = true) }
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage
                    )
                }
                .collect { events ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        favoriteEvents = events
                    )
                }
        }
    }
}
