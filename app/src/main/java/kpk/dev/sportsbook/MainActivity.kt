package kpk.dev.sportsbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import kpk.dev.sports_book_list.di.DaggerSportsBookListComponent
import kpk.dev.sports_book_list.di.SportsBookListComponent
import kpk.dev.sports_book_list.presentation.ui.SportsBookListScreen
import kpk.dev.sportsbook.application.SportsBookApplication
import kpk.dev.sportsbook.di.daggerViewModel
import kpk.dev.sportsbook.ui.SportsBookTheme

class MainActivity : ComponentActivity() {
    private lateinit var sportsBookListComponent: SportsBookListComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = (application as SportsBookApplication).appComponent
        enableEdgeToEdge()
        sportsBookListComponent = DaggerSportsBookListComponent.builder()
            .dataBase(appComponent.getDatabase())
            .api(appComponent.getApi())
            .build()

        setContent {
            SportsBookTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.statusBars.asPaddingValues())
                ) { innerPadding ->
                    val viewModel = daggerViewModel { sportsBookListComponent.getViewModel()}
                    SportsBookListScreen(viewModel, Modifier.padding(innerPadding))
                }
            }
        }
    }
}