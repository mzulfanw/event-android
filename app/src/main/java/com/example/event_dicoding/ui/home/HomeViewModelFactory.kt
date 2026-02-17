package com.example.event_dicoding.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.event_dicoding.domain.repository.EventRepository
import com.example.event_dicoding.domain.usecase.GetActiveEventsUseCase
import com.example.event_dicoding.domain.usecase.GetFinishedEventsUseCase
import com.example.event_dicoding.domain.usecase.SearchEventsUseCase

class HomeViewModelFactory(private val repository: EventRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val getActiveEventsUseCase = GetActiveEventsUseCase(repository)
            val getFinishedEventsUseCase = GetFinishedEventsUseCase(repository)
            val searchEventsUseCase = SearchEventsUseCase(repository)
            return HomeViewModel(
                getActiveEventsUseCase,
                getFinishedEventsUseCase,
                searchEventsUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
