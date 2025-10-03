package kpk.dev.sports_book_list

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kpk.dev.local.entity.FavoriteEvent
import kpk.dev.remote.dto.EventDTO
import kpk.dev.remote.dto.SportDTO
import kpk.dev.sports_book_list.domain.model.Event
import kpk.dev.sports_book_list.domain.model.Sport
import kpk.dev.sports_book_list.domain.repo_contract.SportsBookRepository
import kpk.dev.sports_book_list.domain.usecase.GetSportsBookDataUseCase
import kpk.dev.sports_book_list.domain.usecase.SaveFavoriteEventUseCase
import kpk.dev.sports_book_list.presentation.events.UIEvent
import kpk.dev.sports_book_list.presentation.viewmodel.SportsBookListViewModel
import org.junit.After
import org.junit.Before
import org.junit.Test

class DataFlowTests {

    private val testDispatcher = StandardTestDispatcher()
    private val getSportsBookDataUseCase = GetSportsBookDataUseCase(getRepository())
    private val saveFavoriteEventUseCase = SaveFavoriteEventUseCase(getRepository())

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }


    @Test
    fun `test if when getting initial data as list from the API we are mapping to correct models and the result is returned as a Map for faster lookup`() = runTest {
        val viewModel = SportsBookListViewModel(
            getSportsBookDataUseCase,
            saveFavoriteEventUseCase,
            testDispatcher
        )
        viewModel.loadSportsBook()
        val state = viewModel.state.first { it is UIEvent.SportsBookLoaded }
        val loadedState = state as UIEvent.SportsBookLoaded
        assert(loadedState.data.isNotEmpty())
        assert(loadedState.data.keys.size == 2)
        val firstSport = loadedState.data.keys.first()
        assert((loadedState.data[firstSport] as List<Event>).size == 2)
        assert((loadedState.data.keys.last() as Sport).sportName == "soome_sport_name2")
    }

    @Test
    fun `toggleFilterSportsByFavorites filters only favorite events for a sport`() {
        val viewModel = SportsBookListViewModel(getSportsBookDataUseCase, saveFavoriteEventUseCase)
        viewModel.memoryCacheData = getMockData(true)
        viewModel.favoriteFilterMap[(viewModel.memoryCacheData.keys.first() as Sport).sportId] = false

        viewModel.toggleFilterSportsByFavorites((viewModel.memoryCacheData.keys.first() as Sport), true)

        val state = viewModel.state.value as UIEvent.SportsBookLoaded
        val filteredEvents = state.data[(viewModel.memoryCacheData.keys.first() as Sport)]
        assert(filteredEvents?.size == 1)
        assert(filteredEvents?.all { it.isFavorite } == true)
    }

    @Test
    fun `toggleFilterSportsByFavorites filters all events for a sport`() {
        val viewModel = SportsBookListViewModel(getSportsBookDataUseCase, saveFavoriteEventUseCase)
        viewModel.memoryCacheData = getMockData(true)
        val sport = (viewModel.memoryCacheData.keys.first() as Sport)
        viewModel.favoriteFilterMap[sport.sportId] = true

        viewModel.toggleFilterSportsByFavorites(sport, false)

        val state = viewModel.state.value as UIEvent.SportsBookLoaded
        val filteredEvents = state.data[(viewModel.memoryCacheData.keys.first() as Sport)]
        assert(filteredEvents?.size == 2)
        assert(filteredEvents?.any { !it.isFavorite } == true)
    }

    fun getRepository(): SportsBookRepository {
        return object : SportsBookRepository {
            override suspend fun getSportsBook(): List<SportDTO> {
                return getMockSportsBookDataFromAPI()
            }

            override suspend fun getFavoritesBySport(sportId: String): List<FavoriteEvent> {
                return getMockDataFromDB()
            }

            override suspend fun saveFavorite(event: Event): Long {
                return 1L
            }

            override suspend fun removeFavorite(eventId: String): Int {
                return 1
            }
        }
    }

    fun getMockDataFromDB(): List<FavoriteEvent> {
        return listOf(
            FavoriteEvent(1, "event1", "some_sport_id"),
            FavoriteEvent(2, "event3", "some_sport_id2")
        )
    }

    fun getMockSportsBookDataFromAPI(): List<SportDTO> {
        return listOf(
            SportDTO(
                "some_sport_id",
                "soome_sport_name",
                listOf(
                    EventDTO(
                        "event1",
                        "some_sport_id",
                        "Fighter 1-Fighter 2",
                        startTime = 1759779839
                    ),
                    EventDTO(
                        "event2",
                        "some_sport_id",
                        "Jumper 1-Jumper 2",
                        startTime = 1759779839)
                )
            ),
            SportDTO(
                "some_sport_id2",
                "soome_sport_name2",
                listOf(
                    EventDTO(
                        "event1",
                        "some_sport_id2",
                        "Fighter 1-Fighter 2",
                        startTime = 1759779839
                    ),
                    EventDTO(
                        "event2",
                        "some_sport_id2",
                        "Jumper 1-Jumper 2",
                        startTime = 1759779839)
                )
            )
        )
    }

    fun getMockData(withFavorites: Boolean): MutableMap<Sport, List<Event>> {
        val mockDataMap = mutableMapOf<Sport, List<Event>>()
        val sport = Sport("some_sport_id", "soome_sport_name", hasFavorites = withFavorites)
        val events = listOf(
            Event(
                "event1",
                "some_sport_id",
                "Fighter 1",
                participantTwo = "Fighter 2",
                startTime = 1759779839,
                isFavorite = withFavorites
            ),
            Event(
                "event2",
                "some_sport_id",
                "Jumper 1",
                participantTwo = "Jumper 2",
                startTime = 1759779839,
                isFavorite = false
            )
        )
        mockDataMap[sport] = events
        return mockDataMap
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}