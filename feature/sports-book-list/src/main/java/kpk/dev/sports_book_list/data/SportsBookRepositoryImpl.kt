package kpk.dev.sports_book_list.data

import kpk.dev.local.SportsBookDatabase
import kpk.dev.local.entity.FavoriteEvent
import kpk.dev.remote.api.SportsBookAPI
import kpk.dev.remote.dto.SportDTO
import kpk.dev.sports_book_list.domain.mapper.toDatabaseEntity
import kpk.dev.sports_book_list.domain.model.Event
import kpk.dev.sports_book_list.domain.repo_contract.SportsBookRepository
import javax.inject.Inject

class SportsBookRepositoryImpl @Inject constructor(
    private val database: SportsBookDatabase,
    private val api: SportsBookAPI
) : SportsBookRepository {

    override suspend fun getSportsBook(): List<SportDTO> = api.getSportsBook()

    override suspend fun getFavoritesBySport(sportId: String): List<FavoriteEvent> = database.favoritesDao().getAllBySportType(sportId)

    override suspend fun saveFavorite(event: Event) = database.favoritesDao().insertFavorite(event.toDatabaseEntity())

    override suspend fun removeFavorite(eventId: String) = database.favoritesDao().deleteFavorite(eventId)
}