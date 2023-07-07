package com.almasgali.todoapp

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.WorkManager
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.OneTimeWorkRequestBuilder
import com.almasgali.todoapp.api.network.UpdateFromServerWorker
import com.almasgali.todoapp.api.network.UpdateToServerWorker
import com.almasgali.todoapp.di.component.AppComponent
import com.almasgali.todoapp.di.component.DaggerAppComponent
import java.util.concurrent.TimeUnit

class TodoApp : Application() {

    private lateinit var appComponent: AppComponent

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        startWorker()
    }

    fun getComponent() = appComponent

    private fun startWorker() {
        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(
            PeriodicWorkRequestBuilder<UpdateFromServerWorker>(
                8,
                TimeUnit.HOURS
            ).setConstraints(constraints).build()
        )
    }

    override fun onTerminate() {
        val workManager = WorkManager.getInstance(this)
        workManager
            .enqueue(OneTimeWorkRequestBuilder<UpdateToServerWorker>()
                .setConstraints(constraints)
                .build())
        super.onTerminate()
    }
}
