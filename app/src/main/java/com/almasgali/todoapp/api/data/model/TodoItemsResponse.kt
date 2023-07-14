package com.almasgali.todoapp.api.data.model

import com.almasgali.todoapp.data.model.TodoItemRemote
import com.google.gson.annotations.SerializedName

data class TodoItemsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("list") val list: List<TodoItemRemote>,
    @SerializedName("revision") val revision: Int
)
