package kpk.dev.sportsbook.di

import android.content.Context
import dagger.Component
import kpk.dev.local.SportsBookDatabase
import kpk.dev.remote.api.SportsBookAPI
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, APIModule::class, DatabaseModule::class])
interface ApplicationComponent {

   fun getDatabase(): SportsBookDatabase
   fun getApi(): SportsBookAPI
   fun getContext(): Context
}