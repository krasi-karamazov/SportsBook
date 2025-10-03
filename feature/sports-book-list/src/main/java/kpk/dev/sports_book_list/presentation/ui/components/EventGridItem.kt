package kpk.dev.sports_book_list.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kpk.dev.sports_book_list.domain.model.Event
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


fun getFormattedTime(remainingMillis: Long): String {
    val hours = (remainingMillis / (1_000 * 60 * 60)).toInt()
    val minutes = ((remainingMillis / (1_000 * 60)) % 60).toInt()
    val seconds = ((remainingMillis / 1_000) % 60).toInt()
    return if (remainingMillis > 0) {
        String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        "Started"
    }
}

fun getNextEventTime(event: Event): Calendar {
    // Converting to milliseconds, since it's A UNIX timestamp in seconds
    val eventMillis = event.startTime * 1000
    // Create a Calendar instance in UTC, because I'm assuming the event time is in UTC
    val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        timeInMillis = eventMillis
    }
    // Getting my local timezone and converting the UTC time to local time
    val userCalendar = Calendar.getInstance(TimeZone.getDefault())
    userCalendar.timeInMillis = utcCalendar.timeInMillis
    return userCalendar
}

@Composable
fun EventGridItem(event: Event, currentTimeMillis: Long, onFavoriteToggle: (Boolean) -> Unit = {}) {
    val nextEventTime = getNextEventTime(event).timeInMillis
    val remainingMillis = nextEventTime - currentTimeMillis

    //Building the participants string here so that I can style it without having to utilize multiple Text composables
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(Color.White)) {
            append(event.participantOne)
        }
        withStyle(style = SpanStyle(MaterialTheme.colorScheme.error)) {
            append("\nvs\n")
        }
        withStyle(style = SpanStyle(Color.White)) {
            append(event.participantTwo)
        }
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = getFormattedTime(remainingMillis),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = Color.White
        )
        // Star toggle
        IconToggleButton(
            checked = event.isFavorite,
            onCheckedChange = { isChecked ->
                onFavoriteToggle(isChecked)
            },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = if (event.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "Star",
                tint = if (event.isFavorite) Color.Yellow else Color.Gray
            )
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = annotatedString,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
        )

    }
}