package com.almasgali.todoapp.data

import com.almasgali.todoapp.data.model.TodoItem
import com.almasgali.todoapp.database.dao.TodoDao
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val todoDao: TodoDao) {

    fun getTodoList() = todoDao.getAll()

    fun getTodoListFlow() = todoDao.getAllFlow()

    fun insertTodoItem(todoItem: TodoItem) = todoDao.insert(todoItem)

    fun insertTodoList(todoItems: List<TodoItem>) = todoDao.insertAll(todoItems)

    fun deleteTodoItem(todoItem: TodoItem) = todoDao.delete(todoItem)
}
