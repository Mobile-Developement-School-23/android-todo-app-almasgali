package com.almasgali.todoapp.ui.model

import com.almasgali.todoapp.data.model.TodoItem

data class UIState(
    var data: List<TodoItem>,
    var isHidden: Boolean = false,
    var toEdit: Boolean = false,
    var item: TodoItem? = null,
    val deviceId: String = "pixel2"
)
