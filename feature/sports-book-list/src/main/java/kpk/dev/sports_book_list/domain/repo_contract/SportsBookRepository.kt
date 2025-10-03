package kpk.dev.sports_book_list.domain.repo_contract

import kpk.dev.local.entity.FavoriteEvent
import kpk.dev.remote.dto.SportDTO
import kpk.dev.sports_book_list.domain.model.Event

interface SportsBookRepository {
    suspend fun getSportsBook(): List<SportDTO>
    suspend fun getFavoritesBySport(sportId: String): List<FavoriteEvent>

    suspend fun saveFavorite(event: Event): Long
    suspend fun removeFavorite(eventId: String): Int
}