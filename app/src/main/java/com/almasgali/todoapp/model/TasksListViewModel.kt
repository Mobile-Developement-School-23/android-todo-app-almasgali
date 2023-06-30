package com.almasgali.todoapp.model

import androidx.lifecycle.ViewModel
import com.almasgali.todoapp.util.TodoItem
import com.almasgali.todoapp.util.TodoItemsRepository

class TasksListViewModel: ViewModel() {

    private val list = TodoItemsRepository.getInstance().getList()

    var toEdit = false
    var isHidden = false
    var todoItem: TodoItem? = null

    fun onEditClicked(todoItem: TodoItem) {
        toEdit = true
        this.todoItem = todoItem
    }
}