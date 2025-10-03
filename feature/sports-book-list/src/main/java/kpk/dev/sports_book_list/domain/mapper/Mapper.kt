package kpk.dev.sports_book_list.domain.mapper

import kpk.dev.local.entity.FavoriteEvent
import kpk.dev.remote.dto.EventDTO
import kpk.dev.remote.dto.SportDTO
import kpk.dev.sports_book_list.domain.model.Event
import kpk.dev.sports_book_list.domain.model.Sport

//unused currently, but it's usually best practice the have this logic, just in case
fun Event.toDTO(): EventDTO {
    return EventDTO(
        eventId = id,
        sportId = sportId,
        eventName = "$participantOne-$participantTwo",
        startTime = startTime
    )
}

fun Event.toDatabaseEntity(): FavoriteEvent {
    return FavoriteEvent(
        sportTypeId = this.sportId,
        eventId = this.id,
    )
}

fun EventDTO.toDomain(): Event {
    val names = eventName.split("-")
    val participantOne = names.getOrNull(0)?.trim() ?: "N/A"
    val participantTwo = names.getOrNull(1)?.trim() ?: "N/A"
    return Event(
        id = eventId,
        sportId = sportId,
        participantOne = participantOne,
        participantTwo = participantTwo,
        startTime = startTime
    )
}

fun SportDTO.toDomain(): Sport {
    return Sport(
        sportId = sportId,
        sportName = this.sportName,
        hasFavorites = false
    )
}