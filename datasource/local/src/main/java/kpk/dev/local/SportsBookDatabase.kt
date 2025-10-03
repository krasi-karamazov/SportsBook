package kpk.dev.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kpk.dev.local.dao.FavoritesDao
import kpk.dev.local.entity.FavoriteEvent

@Database(version = 1, entities = [FavoriteEvent::class])
abstract class SportsBookDatabase: RoomDatabase() {
    companion object {
        fun create(context: Context): SportsBookDatabase {
            return Room.databaseBuilder(context, SportsBookDatabase::class.java, "sportsbook.db")
                .build()
        }
    }

    abstract fun favoritesDao(): FavoritesDao
}