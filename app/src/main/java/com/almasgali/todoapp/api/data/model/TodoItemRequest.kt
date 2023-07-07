package com.almasgali.todoapp.api.data.model

import com.almasgali.todoapp.data.model.TodoItemRemote
import com.google.gson.annotations.SerializedName

data class TodoItemRequest(
    @SerializedName("status") val status: String = "ok",
    @SerializedName("element") val element: TodoItemRemote,
)
