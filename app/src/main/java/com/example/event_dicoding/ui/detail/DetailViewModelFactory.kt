package com.example.event_dicoding.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.event_dicoding.data.remote.retrofit.ApiConfig
import com.example.event_dicoding.data.repository.EventRepositoryImpl
import com.example.event_dicoding.domain.usecase.GetEventDetailUseCase

class DetailViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            val apiService = ApiConfig.getApiService()
            val repository = EventRepositoryImpl(apiService)
            val getEventDetailUseCase = GetEventDetailUseCase(repository)
            return DetailViewModel(getEventDetailUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
