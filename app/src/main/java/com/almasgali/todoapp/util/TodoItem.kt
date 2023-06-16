package com.almasgali.todoapp.util

import com.almasgali.todoapp.util.Importance
import java.time.LocalDate
import java.util.Date

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadline: LocalDate?,
    var isDone: Boolean,
    val created: LocalDate,
    val edited: LocalDate?
)