package com.almasgali.todoapp.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class TodoItemRemote(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("importance") val importance: String,
    @SerializedName("deadline") val deadline: Long?,
    @SerializedName("done") var isDone: Boolean,
    @SerializedName("color") var color: String? = null,
    @SerializedName("created_at") val created: Long,
    @SerializedName("changed_at") val edited: Long?,
    @SerializedName("last_updated_by") val lastUpdatedBy: String,
)
