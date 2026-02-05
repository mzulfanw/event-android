package com.example.event_dicoding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.event_dicoding.domain.model.Event
import com.example.event_dicoding.navigation.BottomNavItem
import com.example.event_dicoding.ui.components.ErrorState
import com.example.event_dicoding.ui.components.EventCard
import com.example.event_dicoding.ui.home.HomeViewModel
import com.example.event_dicoding.ui.home.HomeViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentRoute: String,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory()),
    navigateToDetail: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(query) {
        viewModel.searchEvents(query)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        when (currentRoute) {
                            BottomNavItem.Home.route -> "Dicoding Events"
                            BottomNavItem.Upcoming.route -> "Acara yang akan datang"
                            else -> "Acara yang telah selesai"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                placeholder = { Text("Cari event seru...", style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Hapus")
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (uiState.errorMessage != null) {
                    ErrorState(message = uiState.errorMessage!!, onRetry = { viewModel.fetchAllEvents() })
                } else {
                    if (query.isEmpty()) {
                        when (currentRoute) {
                            BottomNavItem.Home.route -> HomeContent(uiState.activeEvents, uiState.finishedEvents, navigateToDetail)
                            BottomNavItem.Upcoming.route -> UpcomingContent(uiState.activeEvents, navigateToDetail)
                            BottomNavItem.Finished.route -> FinishedContent(uiState.finishedEvents, navigateToDetail)
                        }
                    } else {
                        SearchContent(uiState.searchResults, uiState.isSearching, query, navigateToDetail)
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeContent(activeEvents: List<Event>, finishedEvents: List<Event>, navigateToDetail: (Int) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            SectionHeader("Acara yang akan datang")
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(activeEvents, key = { it.id }) { event ->
                    EventCard(
                        event = event,
                        modifier = Modifier
                            .width(300.dp)
                            .clickable { navigateToDetail(event.id) }
                    )
                }
            }
        }

        item {
            SectionHeader("Acara yang telah selesai")
        }

        items(finishedEvents.take(10), key = { it.id }) { event ->
            EventCard(
                event = event,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable { navigateToDetail(event.id) }
            )
        }
    }
}

@Composable
private fun UpcomingContent(activeEvents: List<Event>, navigateToDetail: (Int) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(activeEvents, key = { it.id }) { event ->
            EventCard(
                event = event,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navigateToDetail(event.id) }
            )
        }
    }
}

@Composable
private fun FinishedContent(finishedEvents: List<Event>, navigateToDetail: (Int) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(finishedEvents, key = { it.id }) { event ->
            EventCard(
                event = event,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navigateToDetail(event.id) }
            )
        }
    }
}

@Composable
private fun SearchContent(results: List<Event>, isSearching: Boolean, query: String, navigateToDetail: (Int) -> Unit) {
    if (isSearching) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            LinearProgressIndicator(Modifier.fillMaxWidth().padding(horizontal = 20.dp))
        }
    } else if (results.isNotEmpty()) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(results, key = { it.id }) { event ->
                EventCard(
                    event = event,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigateToDetail(event.id) }
                )
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Text("Tidak ada event yang cocok dengan \"$query\"", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}
