package com.almasgali.todoapp.model

import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.almasgali.todoapp.data.TodoItem
import com.almasgali.todoapp.data.TodoItemsRepository

class TasksListViewModel: ViewModel() {
    var toEdit = false
    var isHidden = false
    var todoItem: TodoItem? = null

    fun onEditClicked(todoItem: TodoItem) {
        toEdit = true
        this.todoItem = todoItem
    }
}