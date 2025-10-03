package kpk.dev.sports_book_list.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kpk.dev.sports_book_list.domain.model.Event
import kpk.dev.sports_book_list.domain.model.Sport
import kpk.dev.sports_book_list.domain.usecase.GetSportsBookDataUseCase
import kpk.dev.sports_book_list.domain.usecase.SaveFavoriteEventUseCase
import kpk.dev.sports_book_list.presentation.events.UIEvent
import java.util.Collections
import java.util.LinkedHashMap
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class SportsBookListViewModel @Inject constructor(
    private val getSportsBookDataUseCase: GetSportsBookDataUseCase,
    private val saveFavoriteEventUseCase: SaveFavoriteEventUseCase,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {
    private val _state = MutableStateFlow<UIEvent>(UIEvent.Idle)
    val state: StateFlow<UIEvent> = _state
    var memoryCacheData = Collections.synchronizedMap(LinkedHashMap<Sport, List<Event>>())
    // Add a map to track filter switch state for each sport
    val favoriteFilterMap = ConcurrentHashMap<String, Boolean>()

    fun loadSportsBook() {
        _state.value = UIEvent.Loading
        println("Loading SportsBook data...")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                println("Loading SportsBook data... 1")
                val data = getSportsBookDataUseCase.execute()
                memoryCacheData.apply {
                    if (isNotEmpty()) {
                        clear()
                    }
                    putAll(data)
                }
                println("Loading SportsBook data... 2")
                withContext(mainDispatcher) {
                    println("Loading SportsBook data... 3")
                    println("main Dispatcher: $mainDispatcher::class.java")
                    _state.value = UIEvent.SportsBookLoaded(data, favoriteFilterMap)
                }
            } catch (e: Exception) {
                withContext(mainDispatcher) {
                    _state.value = UIEvent.Error(e.message ?: "Unknown Error")
                }
            }
        }
    }

    fun saveOrRemoveFavorite(event: Event, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                saveFavoriteEventUseCase.execute(event, isFavorite)
                //Update the memory cache
                val sport = memoryCacheData.keys.firstOrNull { it.sportId == event.sportId }
                sport?.let {
                    val updatedEvents = memoryCacheData[it]?.map { ev ->
                        if (ev.id == event.id) {
                            ev.isFavorite = isFavorite
                        }
                        ev
                    } ?: emptyList()
                    if (isFavorite) {
                        it.hasFavorites = true
                    } else {
                        //If we are removing a favorite we need to check if there are any left
                        if (updatedEvents.none { ev -> ev.isFavorite }) {
                            it.hasFavorites = false
                            // If filter is on for this sport, turn it off
                            favoriteFilterMap[it.sportId] = false
                        }
                    }
                    memoryCacheData[it] = updatedEvents
                }
                // Re-apply filters for all sports before emitting state
                val filteredData = memoryCacheData.mapValues { (sport, events) ->
                    if (favoriteFilterMap[sport.sportId] == true) {
                        events.filter { it.isFavorite }
                    } else {
                        events
                    }
                }
                withContext(mainDispatcher) {
                    _state.value = UIEvent.SportsBookLoaded(filteredData, favoriteFilterMap)
                }
            } catch (e: Exception) {
                withContext(mainDispatcher) {
                    _state.value = UIEvent.Error(e.message ?: "Unknown Error")
                }
            }
        }
    }

    fun toggleFilterSportsByFavorites(sport: Sport, showOnlyFavorites: Boolean) {
        // Update the filter map for the toggled sport
        favoriteFilterMap[sport.sportId] = showOnlyFavorites
        // Re-apply filters for all sports before emitting state
        val filteredData = memoryCacheData.mapValues { (sport, events) ->
            if (favoriteFilterMap[sport.sportId] == true) {
                events.filter { it.isFavorite }
            } else {
                events
            }
        }
        _state.value = UIEvent.SportsBookLoaded(filteredData, favoriteFilterMap)
    }
}