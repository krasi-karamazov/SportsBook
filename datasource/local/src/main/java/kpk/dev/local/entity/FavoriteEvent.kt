package kpk.dev.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
data class FavoriteEvent (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val eventId: String,
    val sportTypeId: String
)