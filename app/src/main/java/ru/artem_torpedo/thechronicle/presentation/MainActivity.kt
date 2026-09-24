package ru.artem_torpedo.thechronicle.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import ru.artem_torpedo.thechronicle.presentation.screens.SubscriptionsScreen
import ru.artem_torpedo.thechronicle.presentation.ui.theme.TheChronicleTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheChronicleTheme {
                SubscriptionsScreen()
            }
        }
    }
}