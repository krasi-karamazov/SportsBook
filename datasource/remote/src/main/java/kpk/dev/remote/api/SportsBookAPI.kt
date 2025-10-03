package kpk.dev.remote.api

import kpk.dev.remote.dto.SportDTO
import retrofit2.http.GET

interface SportsBookAPI {
    @GET("MockSports/sports.json")
    suspend fun getSportsBook(): List<SportDTO>
}