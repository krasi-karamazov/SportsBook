package kpk.dev.sports_book_list.domain.usecase

import kpk.dev.sports_book_list.domain.mapper.toDomain
import kpk.dev.sports_book_list.domain.model.Event
import kpk.dev.sports_book_list.domain.model.Sport
import kpk.dev.sports_book_list.domain.repo_contract.SportsBookRepository
import javax.inject.Inject

class GetSportsBookDataUseCase @Inject constructor(private var repository: SportsBookRepository) {

    suspend fun execute(): Map<Sport, List<Event>> {
        /**
         * Loading the sports and events and checking if there are any favorites in the db.
         * Since the list is not backed by a db on the backend and we can't really set
         * favorites by session/account we can just do it on startup
         * and let the UI state handle runtime changes
        **/
        val sportsMap = mutableMapOf<Sport, List<Event>>()
        repository.getSportsBook().map {
            val favoriteEventsBySport = repository.getFavoritesBySport(it.sportId)
            val domainSport = it.toDomain().apply {
                if (favoriteEventsBySport.isNotEmpty()) {
                    hasFavorites = true
                }
            }
            val events = it.events.map { event ->
                val domainEvent = event.toDomain()
                if (favoriteEventsBySport.any { fav -> fav.eventId == domainEvent.id }) {
                    domainEvent.isFavorite = true
                }
                domainEvent
            }


            sportsMap.put(domainSport, events)
        }
        return sportsMap
    }
}