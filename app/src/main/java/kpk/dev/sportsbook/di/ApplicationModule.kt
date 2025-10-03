package kpk.dev.sportsbook.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ApplicationModule(private val applicationContext: Context) {

    @Provides
    @Singleton
    fun provideAppContext() = applicationContext

}