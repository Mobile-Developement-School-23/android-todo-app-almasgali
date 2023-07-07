package com.almasgali.todoapp.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import com.almasgali.todoapp.data.model.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todoitems")
    fun getAll(): List<TodoItem>

    @Query("SELECT * FROM todoitems")
    fun getAllFlow(): Flow<List<TodoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(todoEntities: List<TodoItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(todoEntity: TodoItem)

    @Delete
    fun delete(todoEntity: TodoItem)
}
