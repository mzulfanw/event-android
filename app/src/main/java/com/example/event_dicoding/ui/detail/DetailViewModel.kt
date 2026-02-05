package com.example.event_dicoding.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.event_dicoding.domain.usecase.GetEventDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getEventDetailUseCase: GetEventDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun getEventDetail(id: Int) {
        viewModelScope.launch {
            getEventDetailUseCase(id)
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
                }
        }
    }
}
