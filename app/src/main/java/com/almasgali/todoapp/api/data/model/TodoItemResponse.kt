package com.almasgali.todoapp.api.data.model

import com.almasgali.todoapp.data.model.TodoItemRemote
import com.google.gson.annotations.SerializedName

data class TodoItemResponse(
    @SerializedName("status") val status: String,
    @SerializedName("element") val element: TodoItemRemote,
    @SerializedName("revision") var revision: Int
)
