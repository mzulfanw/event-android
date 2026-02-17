package com.example.event_dicoding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.event_dicoding.data.local.room.AppDatabase
import com.example.event_dicoding.data.remote.retrofit.ApiConfig
import com.example.event_dicoding.data.repository.EventRepositoryImpl
import com.example.event_dicoding.ui.components.ErrorState
import com.example.event_dicoding.ui.components.EventCard
import com.example.event_dicoding.ui.favorite.FavoriteViewModel
import com.example.event_dicoding.ui.favorite.FavoriteViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit
) {
    val context = LocalContext.current
    val apiService = ApiConfig.getApiService()
    val database = AppDatabase.getInstance(context)
    val repository = EventRepositoryImpl(apiService, database.favoriteEventDao())
    val viewModel: FavoriteViewModel = viewModel(factory = FavoriteViewModelFactory(repository))
    
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Acara Favorit", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.errorMessage != null -> {
                    ErrorState(message = uiState.errorMessage!!, onRetry = {  })
                }

                uiState.favoriteEvents.isEmpty() -> {
                    Text(
                        text = "Belum ada event favorit",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.favoriteEvents, key = { it.id }) { event ->
                            EventCard(
                                event = event,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { navigateToDetail(event.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
