package ru.artem_torpedo.thechronicle.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.artem_torpedo.thechronicle.data.repository.NewsRepositoryImpl
import ru.artem_torpedo.thechronicle.presentation.ui.theme.TheChronicleTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var repo : NewsRepositoryImpl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        lifecycleScope.launch {
            repo.addNewSubscription("Putin")
            repo.updateArticlesForNewTopic("Putin")
        }
        setContent {
            TheChronicleTheme {

            }
        }
    }
}