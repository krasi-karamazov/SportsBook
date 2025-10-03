package kpk.dev.sports_book_list.domain.model

data class Event (
    val id: String,
    val sportId: String,
    val participantOne: String,
    val participantTwo: String,
    val startTime: Long,
    var isFavorite: Boolean = false
)