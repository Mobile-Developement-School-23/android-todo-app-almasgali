package com.almasgali.todoapp.ui.model

import com.almasgali.todoapp.data.model.TodoItem

data class UIState(
    var data: List<TodoItem>,
    val deviceId: String = "pixel2"
)
