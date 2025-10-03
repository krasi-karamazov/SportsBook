package kpk.dev.sports_book_list.di

import dagger.BindsInstance
import dagger.Component
import kpk.dev.local.SportsBookDatabase
import kpk.dev.remote.api.SportsBookAPI
import kpk.dev.sports_book_list.di.scope.SportsBookListScope
import kpk.dev.sports_book_list.presentation.viewmodel.SportsBookListViewModel

@SportsBookListScope
@Component(modules = [SportsBookListModule::class])
interface SportsBookListComponent {

    fun getViewModel(): SportsBookListViewModel


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun dataBase(database: SportsBookDatabase): Builder
        @BindsInstance
        fun api(api: SportsBookAPI): Builder
        fun build(): SportsBookListComponent
    }
}