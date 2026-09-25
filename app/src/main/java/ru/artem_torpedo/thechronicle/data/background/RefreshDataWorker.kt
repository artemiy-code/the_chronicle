package ru.artem_torpedo.thechronicle.data.background

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ru.artem_torpedo.thechronicle.domain.useCases.UpdateAllArticlesUseCase

class RefreshDataWorker(
    context: Context,
    params: WorkerParameters,
    private val updateAllArticlesUseCase: UpdateAllArticlesUseCase,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("RefreshDataWorker", "Start")
        updateAllArticlesUseCase()
        Log.d("RefreshDataWorker", "End")
        return Result.success()
    }
}