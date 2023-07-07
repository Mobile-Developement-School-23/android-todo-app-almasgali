package com.almasgali.todoapp.api.data

import com.almasgali.todoapp.data.TodoItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoItemsRequest(
    @SerialName("status") val status: String,
    @SerialName("list") val list: List<TodoItem>,
)
