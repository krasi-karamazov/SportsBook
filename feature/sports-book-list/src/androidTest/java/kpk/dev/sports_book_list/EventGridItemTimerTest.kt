package kpk.dev.sports_book_list

import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import kpk.dev.sports_book_list.domain.model.Event
import kpk.dev.sports_book_list.presentation.ui.components.EventGridItem
import org.junit.Rule
import org.junit.Test

class EventGridItemTimerTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun countdownTimerUpdatesTest() {
        val baseTime = 0L //Using this guy for simplicity, I really don't want to calculate offsets from the actual current time :)
        val currentTimeMillis = mutableStateOf(baseTime)
        val event = Event(
            id = "1",
            startTime = 10, // going in the future here, while remembering we're looking for seconds, not millis...
            participantOne = "Fighter 1",
            participantTwo = "Fighter 2",
            isFavorite = false,
            sportId = "Boxing"
        )

        composeTestRule.setContent {
            EventGridItem(event = event, currentTimeMillis = currentTimeMillis.value, onFavoriteToggle = {})
        }


        // The countdown has begun
        composeTestRule.onNodeWithText("00:00:10").assertExists()

        // 5 seconds left...
        composeTestRule.runOnUiThread { currentTimeMillis.value = baseTime + 5_000 }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("00:00:05").assertExists()

        // Liftoff!!!
        composeTestRule.runOnUiThread { currentTimeMillis.value = baseTime + 10_000 }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Started").assertExists()
    }
}