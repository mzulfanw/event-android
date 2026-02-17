package com.example.event_dicoding.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.event_dicoding.domain.model.Event
import com.example.event_dicoding.domain.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun getEventDetail(id: Int) {
        viewModelScope.launch {
            repository.getEventDetail(id)
                .onStart {
                    _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                }
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Gagal memuat detail event"
                    )
                }
                .collect { event ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        event = event
                    )
                    checkFavoriteStatus(id)
                }
        }
    }

    private fun checkFavoriteStatus(id: Int) {
        viewModelScope.launch {
            repository.isFavorite(id).collect {
                _isFavorite.value = it
            }
        }
    }

    fun toggleFavorite(event: Event) {
        viewModelScope.launch {
            if (_isFavorite.value) {
                repository.deleteFavorite(event)
            } else {
                repository.insertFavorite(event)
            }
        }
    }
}
