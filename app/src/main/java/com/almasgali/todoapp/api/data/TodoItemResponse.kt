package com.almasgali.todoapp.api.data

import com.almasgali.todoapp.data.TodoItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoItemResponse(
    @SerialName("status") val status: String,
    @SerialName("element") val element: TodoItem,
    @SerialName("revision") var revision: Int = -1
)
