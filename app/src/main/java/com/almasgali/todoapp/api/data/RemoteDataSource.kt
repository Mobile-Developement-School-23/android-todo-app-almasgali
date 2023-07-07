package com.almasgali.todoapp.api.data

import com.almasgali.todoapp.api.data.model.TodoItemRequest
import com.almasgali.todoapp.api.data.model.TodoItemsRequest
import com.almasgali.todoapp.api.services.TodoService
import com.almasgali.todoapp.data.model.TodoItem
import com.almasgali.todoapp.util.fromLocalToRemote
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val todoService: TodoService) {

    private var currentRevision = 1

    fun setRevision(revision: Int) {
        currentRevision = revision
    }

    suspend fun getTodoList() = todoService.getList()

    suspend fun patchTodoList(todoItems: List<TodoItem>) =
        todoService.patchList(
            currentRevision,
            TodoItemsRequest(list = todoItems.map { fromLocalToRemote(it) })
        )

    suspend fun postTodoItem(todoItem: TodoItem) =
        todoService.postItem(
            currentRevision,
            TodoItemRequest(element = fromLocalToRemote(todoItem))
        )

    suspend fun putTodoItem(todoItem: TodoItem) =
        todoService.putItem(
            currentRevision,
            todoItem.id,
            TodoItemRequest(element = fromLocalToRemote(todoItem))
        )

    suspend fun deleteTodoItem(todoItem: TodoItem) =
        todoService.deleteItem(currentRevision, todoItem.id)
}
