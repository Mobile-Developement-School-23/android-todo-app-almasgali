package com.almasgali.todoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "todoitems")
data class TodoItem(
    @PrimaryKey @SerialName("id") val id: String,
    @SerialName("text") val text: String,
    @SerialName("importance") val importance: Importance,
    @SerialName("deadline") val deadline: Long?,
    @SerialName("done") var isDone: Boolean,
    @SerialName("color") var color: String? = null,
    @SerialName("created_at") val created: Long,
    @SerialName("changed_at") val edited: Long?,
    @SerialName("last_updated_by") val lastUpdateBy: String,
)