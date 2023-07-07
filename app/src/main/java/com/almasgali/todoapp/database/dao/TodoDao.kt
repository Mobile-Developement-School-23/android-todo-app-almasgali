package com.almasgali.todoapp.database.dao

import androidx.room.*
import com.almasgali.todoapp.data.TodoItem

@Dao
interface TodoDao {
    @Query("SELECT * FROM todoitems")
    fun getAll(): List<TodoItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(todoEntities: List<TodoItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(todoEntity: TodoItem)

    @Delete
    fun delete(todoEntity: TodoItem)
}