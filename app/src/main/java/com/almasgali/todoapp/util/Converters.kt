package com.almasgali.todoapp.util

import com.almasgali.todoapp.data.model.Importance
import com.almasgali.todoapp.data.model.TodoItem
import com.almasgali.todoapp.data.model.TodoItemRemote


private fun mapper(str: String) = when (str) {
    "low" -> Importance.LOW
    "important" -> Importance.HIGH
    else -> Importance.MEDIUM
}

private fun mapper(importance: Importance) = when (importance) {
    Importance.LOW -> "low"
    Importance.HIGH -> "important"
    else -> "basic"
}

fun fromRemoteToLocal(todoItemRemote: TodoItemRemote): TodoItem {
    return TodoItem(
        todoItemRemote.id,
        todoItemRemote.text,
        mapper(todoItemRemote.importance),
        todoItemRemote.deadline,
        todoItemRemote.isDone,
        todoItemRemote.color,
        todoItemRemote.created,
        todoItemRemote.edited,
        todoItemRemote.lastUpdatedBy
    )
}

fun fromLocalToRemote(todoItem: TodoItem): TodoItemRemote {
    return TodoItemRemote(
        todoItem.id,
        todoItem.text,
        mapper(todoItem.importance),
        todoItem.deadline,
        todoItem.isDone,
        todoItem.color,
        todoItem.created,
        todoItem.edited,
        todoItem.lastUpdatedBy
    )
}
