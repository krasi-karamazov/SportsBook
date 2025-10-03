package kpk.dev.remote.dto

import com.google.gson.annotations.SerializedName

data class EventDTO (
    @SerializedName("i")
    val eventId: String,
    @SerializedName("si")
    val sportId: String,
    @SerializedName("d")
    val eventName: String,
    @SerializedName("tt")
    val startTime: Long,
)