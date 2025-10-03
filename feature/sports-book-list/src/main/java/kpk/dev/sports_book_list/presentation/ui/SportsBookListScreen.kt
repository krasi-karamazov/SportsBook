package kpk.dev.sports_book_list.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kpk.dev.sports_book_list.presentation.events.UIEvent
import kpk.dev.sports_book_list.presentation.ui.components.SportsBookList
import kpk.dev.sports_book_list.presentation.viewmodel.SportsBookListViewModel

@Composable
fun SportsBookListScreen(viewModel: SportsBookListViewModel, modifier: Modifier) {
    // Hoisting this here so that we have a single ticker for the whole screen instead of one per item
    val currentTime = remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        viewModel.loadSportsBook()
        while (true) {
            delay(1_000)
            currentTime.longValue = System.currentTimeMillis()
        }
    }

    val uiState by viewModel.state.collectAsState()

    when (uiState) {
        is UIEvent.SportsBookLoaded -> {
            val loadedState = uiState as UIEvent.SportsBookLoaded
            if(loadedState.data.isEmpty().not()) {
                SportsBookList(
                    modifier = modifier,
                    data = loadedState.data,
                    currentTime = currentTime.longValue,
                    onEventFavoriteToggle = { event, isFavorite ->
                        viewModel.saveOrRemoveFavorite(event, isFavorite)
                    },
                    onSportFavoritesToggle = { sport, showOnlyFavorites ->
                        viewModel.toggleFilterSportsByFavorites(sport, showOnlyFavorites)
                    },
                    favoriteFilterMap = loadedState.favoriteFilterMap
                )
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No data available")
                }
            }
        }
        is UIEvent.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is UIEvent.Error -> {
            val error = (uiState as UIEvent.Error).message
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: $error", color = Color.Red)
            }
        }
        else -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No data available")
            }
        }
    }
}