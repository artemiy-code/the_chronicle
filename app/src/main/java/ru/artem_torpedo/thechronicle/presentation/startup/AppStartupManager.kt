package ru.artem_torpedo.thechronicle.presentation.startup

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.artem_torpedo.thechronicle.domain.useCases.service.StartServiceUseCase
import javax.inject.Inject

class AppStartupManager @Inject constructor(
    private val startServiceUseCase: StartServiceUseCase,
) {
    val scope = CoroutineScope(Dispatchers.IO)

    fun startService() {
        scope.launch {
            startServiceUseCase()
        }
    }
}