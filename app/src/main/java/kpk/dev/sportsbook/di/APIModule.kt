package kpk.dev.sportsbook.di

import dagger.Module
import dagger.Provides
import kpk.dev.remote.api.SportsBookAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class APIModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://ios-kaizen.github.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideSportsBookAPI(retrofit: Retrofit): SportsBookAPI =
        retrofit.create(SportsBookAPI::class.java)

}