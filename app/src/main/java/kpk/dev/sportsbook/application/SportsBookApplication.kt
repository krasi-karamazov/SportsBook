package kpk.dev.sportsbook.application

import android.app.Application
import kpk.dev.sportsbook.di.ApplicationComponent
import kpk.dev.sportsbook.di.ApplicationModule
import kpk.dev.sportsbook.di.DaggerApplicationComponent

class SportsBookApplication : Application() {
    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(applicationContext)).build()

    }
}