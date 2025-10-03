package kpk.dev.sports_book_list.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kpk.dev.sports_book_list.domain.model.Event
import kpk.dev.sports_book_list.domain.model.Sport

@Composable
fun SportsBookList(
    data: Map<Sport, List<Event>>,
    currentTime: Long,
    onEventFavoriteToggle: (Event, Boolean) -> Unit,
    onSportFavoritesToggle: (Sport, Boolean) -> Unit,
    favoriteFilterMap: Map<String, Boolean>,
    modifier: Modifier = Modifier
) {
    val sports = data.keys.toList()
    val expandedMap = remember {
        mutableStateMapOf<String, Boolean>()
    }
    LaunchedEffect(sports) {
        val currentSportIds = sports.map { it.sportId }.toSet()
        sports.forEach { sport ->
            if (expandedMap.containsKey(sport.sportId).not()) {
                expandedMap[sport.sportId] = true
            }
        }
        expandedMap.keys.toList().forEach { key ->
            if (key !in currentSportIds) expandedMap.remove(key)
        }
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        sports.forEachIndexed { sportIndex, sport ->
            val events = data[sport] ?: emptyList()
            val isExpanded = expandedMap[sport.sportId] ?: true
            val showOnlyFavorites = favoriteFilterMap[sport.sportId] ?: false
            stickyHeader(key = "header_${sport.sportId}_$sportIndex") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(MaterialTheme.colorScheme.secondary) // White header
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sport.sportName,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold
                    )
                    Switch(
                        enabled = sport.hasFavorites,
                        checked = showOnlyFavorites,
                        onCheckedChange = {
                            onSportFavoritesToggle(sport, it)
                        },
                        colors = SwitchDefaults.colors(
                            uncheckedTrackColor = Color.LightGray
                        ),
                        thumbContent = {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        color = if (showOnlyFavorites) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = if (showOnlyFavorites) Icons.Filled.Star else Icons.Filled.StarBorder,
                                    contentDescription = if (showOnlyFavorites) "Starred" else "Not Starred",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    )
                    IconButton(onClick = {
                        expandedMap[sport.sportId] = !isExpanded
                    }) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = if (isExpanded) "Collapse" else "Expand"
                        )
                    }
                }
            }
            // Only show grid rows if expanded, otherwise just the header
            if (isExpanded) {
                events.chunked(4).forEachIndexed { rowIndex, rowEvents ->
                    item(key = "${sport.sportId}_row_${rowIndex}_$sportIndex") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowEvents.forEach { event ->
                                Box(modifier = Modifier.weight(1f)) {
                                    EventGridItem(event, currentTime) { isStarred ->
                                        onEventFavoriteToggle(event, isStarred)
                                    }
                                }
                            }
                            // repeating here so that we always have 4 items in a row, otherwise it looks a little strange...
                            repeat(4 - rowEvents.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }

}