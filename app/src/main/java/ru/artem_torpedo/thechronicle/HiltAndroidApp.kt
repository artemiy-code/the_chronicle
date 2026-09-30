package ru.artem_torpedo.thechronicle

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import ru.artem_torpedo.thechronicle.presentation.startup.AppStartupManager
import javax.inject.Inject

@HiltAndroidApp
class HiltAndroidApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var appStartup: AppStartupManager

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        appStartup.startService()
    }
}