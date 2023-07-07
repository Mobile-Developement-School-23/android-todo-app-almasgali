package com.almasgali.todoapp.api.data

import com.almasgali.todoapp.data.TodoItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoItemRequest(
    @SerialName("element") val element: TodoItem,
)
