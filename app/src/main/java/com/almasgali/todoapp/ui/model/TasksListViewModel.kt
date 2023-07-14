package com.almasgali.todoapp.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.almasgali.todoapp.data.TodoItemsRepository
import com.almasgali.todoapp.data.model.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class TasksListViewModel @Inject constructor(private val todoItemsRepository: TodoItemsRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(UIState(todoItemsRepository.getList()))
    lateinit var deletedItem: TodoItem
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()
    var deleted = false

    init {
        viewModelScope.launch {
            todoItemsRepository.getListFlow().collect {
                _uiState.value.data = it
            }
        }
    }

    fun onHideItem(todoItem: TodoItem) {
        viewModelScope.launch {
            todoItemsRepository.edit(
                TodoItem(
                    todoItem.id,
                    todoItem.text,
                    todoItem.importance,
                    todoItem.deadline,
                    !todoItem.isDone,
                    todoItem.color,
                    todoItem.created,
                    LocalDate.now().toEpochDay(),
                    _uiState.value.deviceId
                )
            )
        }
    }

    fun delete(todoItem: TodoItem) {
        deletedItem = todoItem
        deleted = true
        viewModelScope.launch {
            todoItemsRepository.delete(todoItem)
        }
    }

    fun edit(todoItem: TodoItem) {
        viewModelScope.launch {
            todoItemsRepository.edit(todoItem)
        }
    }

    fun add(todoItem: TodoItem) {
        viewModelScope.launch {
            todoItemsRepository.add(todoItem)
        }
    }
}
