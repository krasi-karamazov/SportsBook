package kpk.dev.sports_book_list.domain.usecase

import kpk.dev.sports_book_list.domain.model.Event
import kpk.dev.sports_book_list.domain.repo_contract.SportsBookRepository
import javax.inject.Inject

class SaveFavoriteEventUseCase @Inject constructor(private var repository: SportsBookRepository) {
    suspend fun execute(event: Event, isFavorite: Boolean) {
        if(isFavorite) {
            repository.saveFavorite(event)
        } else {
            repository.removeFavorite(event.id)
        }

    }
}