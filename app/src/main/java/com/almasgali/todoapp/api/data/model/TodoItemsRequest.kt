package com.almasgali.todoapp.api.data.model

import com.almasgali.todoapp.data.model.TodoItemRemote
import com.google.gson.annotations.SerializedName

data class TodoItemsRequest(
    @SerializedName("status") val status: String = "ok",
    @SerializedName("list") val list: List<TodoItemRemote>,
)
