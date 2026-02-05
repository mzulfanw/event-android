package com.example.event_dicoding.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.event_dicoding.domain.usecase.GetActiveEventsUseCase
import com.example.event_dicoding.domain.usecase.GetFinishedEventsUseCase
import com.example.event_dicoding.domain.usecase.SearchEventsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getActiveEventsUseCase: GetActiveEventsUseCase,
    private val getFinishedEventsUseCase: GetFinishedEventsUseCase,
    private val searchEventsUseCase: SearchEventsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        fetchAllEvents()
    }

    fun fetchAllEvents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            try {
                launch {
                    getActiveEventsUseCase()
                        .catch { e -> handleFailure(e) }
                        .collect { events ->
                            _uiState.value = _uiState.value.copy(activeEvents = events, isLoading = false)
                        }
                }
                launch {
                    getFinishedEventsUseCase()
                        .catch { e -> handleFailure(e) }
                        .collect { events ->
                            _uiState.value = _uiState.value.copy(finishedEvents = events, isLoading = false)
                        }
                }
            } catch (e: Exception) {
                handleFailure(e)
            }
        }
    }

    fun searchEvents(query: String) {
        if (query.isEmpty()) {
            _uiState.value = _uiState.value.copy(searchResults = emptyList(), isSearching = false)
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            searchEventsUseCase(query)
                .onStart { _uiState.value = _uiState.value.copy(isSearching = true) }
                .catch { e -> handleFailure(e) }
                .collect { events ->
                    _uiState.value = _uiState.value.copy(
                        searchResults = events,
                        isSearching = false
                    )
                }
        }
    }

    private fun handleFailure(e: Throwable) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            isSearching = false,
            errorMessage = e.localizedMessage ?: "Terjadi kesalahan koneksi"
        )
    }
}
