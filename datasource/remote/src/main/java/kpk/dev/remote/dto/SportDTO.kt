package kpk.dev.remote.dto

import com.google.gson.annotations.SerializedName

data class SportDTO (
    @SerializedName("i")
    val sportId: String,
    @SerializedName("d")
    val sportName: String,
    @SerializedName("e")
    val events: List<EventDTO>,
)