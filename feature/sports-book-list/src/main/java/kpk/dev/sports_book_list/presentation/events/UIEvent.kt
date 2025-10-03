package kpk.dev.sports_book_list.presentation.events

import kpk.dev.sports_book_list.domain.model.Event
import kpk.dev.sports_book_list.domain.model.Sport

sealed class UIEvent {
    object Idle : UIEvent()
    object Loading : UIEvent()
    class Error(val message: String) : UIEvent()
    class SportsBookLoaded(val data: Map<Sport, List<Event>>, val favoriteFilterMap: Map<String, Boolean>) : UIEvent()
}