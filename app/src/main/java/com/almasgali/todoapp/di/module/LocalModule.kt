package com.almasgali.todoapp.di.module

import android.content.Context
import androidx.room.Room
import com.almasgali.todoapp.database.TodoDatabase
import com.almasgali.todoapp.database.dao.TodoDao
import dagger.Module
import dagger.Provides


@Module
class LocalModule {

    @Provides
    fun provideTodoDao(context: Context): TodoDao {
        return Room
            .databaseBuilder(context, TodoDatabase::class.java, "todoitems")
            .allowMainThreadQueries()
            .build()
            .todoDao()
    }
}
