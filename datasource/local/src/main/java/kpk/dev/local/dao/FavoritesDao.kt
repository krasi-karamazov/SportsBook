package kpk.dev.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kpk.dev.local.entity.FavoriteEvent

@Dao
interface FavoritesDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertFavorite(favoriteEvent: FavoriteEvent): Long

    @Query("SELECT * FROM favorite_events WHERE sportTypeId = :sportTypeId")
    suspend fun getAllBySportType(sportTypeId: String): List<FavoriteEvent>

    @Query("SELECT * FROM favorite_events")
    suspend fun getAllFavorites(): List<FavoriteEvent>

    @Query("SELECT COUNT(*) FROM favorite_events WHERE sportTypeId = :sportTypeId")
    suspend fun getCountOfEventsBySportType(sportTypeId: String): Int

    @Query("DElETE FROM favorite_events WHERE eventId = :favoriteEventId")
    suspend fun deleteFavorite(favoriteEventId: String): Int
}