package com.almasgali.todoapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.almasgali.todoapp.data.model.TodoItem
import com.almasgali.todoapp.database.dao.TodoDao

@Database(entities = [TodoItem::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}
