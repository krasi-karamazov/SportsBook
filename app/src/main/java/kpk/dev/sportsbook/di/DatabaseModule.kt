package kpk.dev.sportsbook.di

import android.content.Context
import dagger.Module
import dagger.Provides
import kpk.dev.local.SportsBookDatabase
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(applicationContext: Context): SportsBookDatabase =
        SportsBookDatabase.create(applicationContext)
}