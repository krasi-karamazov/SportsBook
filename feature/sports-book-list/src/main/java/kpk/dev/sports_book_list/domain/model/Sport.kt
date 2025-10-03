package kpk.dev.sports_book_list.domain.model

data class Sport (
    val sportId: String,
    val sportName: String,
    var hasFavorites: Boolean
)