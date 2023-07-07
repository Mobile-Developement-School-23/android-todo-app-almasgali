package com.almasgali.todoapp.di.component

import android.content.Context
import com.almasgali.todoapp.data.TodoItemsRepository
import com.almasgali.todoapp.di.module.LocalModule
import com.almasgali.todoapp.di.module.RemoteModule
import com.almasgali.todoapp.ui.model.TasksListViewModel
import com.almasgali.todoapp.ui.model.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [LocalModule::class, RemoteModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance context: Context): AppComponent
    }

    fun viewModelsFactory(): ViewModelFactory

    fun repository(): TodoItemsRepository

    fun viewModel(): TasksListViewModel
}
