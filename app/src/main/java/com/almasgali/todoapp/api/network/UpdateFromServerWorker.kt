package com.almasgali.todoapp.api.network

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.almasgali.todoapp.TodoApp

class UpdateFromServerWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val todoItemsRepository =
            (context.applicationContext as TodoApp)
                .getComponent()
                .repository()
        todoItemsRepository.updateFromServer()
        Log.d("WORKER", "Updated from worker")
        return Result.success()
    }
}
