package ru.artem_torpedo.thechronicle.data.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.artem_torpedo.thechronicle.domain.useCases.mainScreen.UpdateAllArticlesUseCase

@HiltWorker
class RefreshDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val updateAllArticlesUseCase: UpdateAllArticlesUseCase,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("RefreshDataWorker", "Start")
        updateAllArticlesUseCase()
        Log.d("RefreshDataWorker", "End")
        return Result.success()
    }
}