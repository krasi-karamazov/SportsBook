package kpk.dev.sports_book_list.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kpk.dev.local.SportsBookDatabase
import kpk.dev.remote.api.SportsBookAPI
import kpk.dev.sports_book_list.data.SportsBookRepositoryImpl
import kpk.dev.sports_book_list.di.scope.SportsBookListScope
import kpk.dev.sports_book_list.domain.repo_contract.SportsBookRepository
import kpk.dev.sports_book_list.domain.usecase.GetSportsBookDataUseCase
import kpk.dev.sports_book_list.domain.usecase.SaveFavoriteEventUseCase

@Module
class SportsBookListModule {

    @SportsBookListScope
    @Provides
    fun providerSportsBookRepository(
        database: SportsBookDatabase,
        api: SportsBookAPI
    ): SportsBookRepository = SportsBookRepositoryImpl(database, api)

    @SportsBookListScope
    @Provides
    fun provideGetSportsBookDataUseCase(repository: SportsBookRepository) =
        GetSportsBookDataUseCase(repository)

    @SportsBookListScope
    @Provides
    fun provideSaveFavoriteEventUseCase(repository: SportsBookRepository) =
        SaveFavoriteEventUseCase(repository)

    @SportsBookListScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}