package com.almasgali.todoapp.util

import java.time.LocalDate

class TodoItemsRepository private constructor() {

    companion object {
        @Volatile
        private var instance: TodoItemsRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: TodoItemsRepository().also { instance = it }
            }
    }

    private val list: ArrayList<TodoItem> = ArrayList(
        listOf(
            TodoItem(
                "1",
                "some text",
                Importance.LOW,
                null,
                false,
                LocalDate.now(),
                null
            ),
            TodoItem(
                "2",
                "another text",
                Importance.MEDIUM,
                null,
                false,
                LocalDate.now(),
                null
            ),
            TodoItem(
                "3",
                "some text",
                Importance.HIGH,
                null,
                false,
                LocalDate.now(),
                null
            ),
            TodoItem(
                "4",
                "some very very very very very very very very very very very very very very very very very very very very very very very long text",
                Importance.LOW,
                null,
                false,
                LocalDate.now(),
                null
            ),
            TodoItem(
                "5",
                "some very very very very very very very very very very very very very very very very very very very very very very very long text",
                Importance.MEDIUM,
                null,
                false,
                LocalDate.now(),
                null
            ),
            TodoItem(
                "6",
                "some very very very very very very very very very very very very very very very very very very very very very very very long text",
                Importance.HIGH,
                null,
                false,
                LocalDate.now(),
                null
            )
        )
    )

    private var id = list.size + 1;

    fun getList() = ArrayList(list)

    fun add(todoItem: TodoItem) {
        list.add(todoItem)
    }

    fun edit(todoItem: TodoItem) {
        for (i in 0 until list.size) {
            if (list[i].id == todoItem.id) {
                list[i] = todoItem
                break
            }
        }
    }

    fun delete(todoItem: TodoItem) {
        list.remove(todoItem)
    }

    fun getId() = (id++).toString();
}