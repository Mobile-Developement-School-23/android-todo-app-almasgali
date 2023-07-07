package com.almasgali.todoapp.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.almasgali.todoapp.data.TodoItemsRepository
import com.almasgali.todoapp.data.model.Importance
import com.almasgali.todoapp.data.model.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class TasksListViewModel @Inject constructor(private val todoItemsRepository: TodoItemsRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(UIState(todoItemsRepository.getList()))
    val uiState: StateFlow<UIState> = _uiState

    init {
        viewModelScope.launch {
            todoItemsRepository.getListFlow().collect {
                if (!_uiState.value.isHidden) {
                    _uiState.value.data = it
                } else {
                    _uiState.value.data = it.filter { item -> !item.isDone }
                }
            }
        }
    }

    fun toEdit() = _uiState.value.toEdit

    fun onEditClicked(todoItem: TodoItem) {
        _uiState.value.toEdit = true
        _uiState.value.item = todoItem
    }

    fun onCloseClicked() {
        _uiState.value.toEdit = false
    }

    fun onHideClicked(currentState: Boolean) {
        _uiState.value.isHidden = currentState
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

    fun delete() {
        viewModelScope.launch {
            todoItemsRepository.delete(_uiState.value.item!!)
        }
        _uiState.value.toEdit = false
    }

    fun update(text: String, importance: Importance, deadline: Long?) {
        if (_uiState.value.toEdit) {
            _uiState.value.toEdit = false
            viewModelScope.launch {
                todoItemsRepository.edit(
                    TodoItem(
                        _uiState.value.item!!.id,
                        text,
                        importance,
                        deadline,
                        _uiState.value.item!!.isDone,
                        null,
                        _uiState.value.item!!.created,
                        LocalDate.now().toEpochDay(),
                        _uiState.value.deviceId
                    )
                )
            }
        } else {
            val currentTime = LocalDate.now().toEpochDay()
            viewModelScope.launch {
                todoItemsRepository.add(
                    TodoItem(
                        UUID.randomUUID().toString(),
                        text,
                        importance,
                        deadline,
                        false,
                        null,
                        currentTime,
                        currentTime,
                        _uiState.value.deviceId
                    )
                )
            }
        }
    }
}
