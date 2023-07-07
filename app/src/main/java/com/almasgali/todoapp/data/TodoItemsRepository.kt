package com.almasgali.todoapp.data

import android.content.Context
import androidx.room.Room
import com.almasgali.todoapp.api.data.TodoItemsRequest
import com.almasgali.todoapp.api.network.TodoClient
import com.almasgali.todoapp.database.TodoDatabase
import com.almasgali.todoapp.database.dao.TodoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

class TodoItemsRepository private constructor(context: Context) {

    companion object : SingletonHolder<TodoItemsRepository, Context>(::TodoItemsRepository)

    private var list: ArrayList<TodoItem> = ArrayList()
    private val todoClient = TodoClient()
    private val todoDao: TodoDao

    init {
        todoDao = Room.databaseBuilder(context, TodoDatabase::class.java, "todoitems").build().todoDao()
    }

    suspend fun updateFromServer() {
        withContext(Dispatchers.IO) {
            val fromServer = todoClient.getFromServer()
            todoDao.insertAll(fromServer)
            list = ArrayList(todoDao.getAll())
        }
    }

    suspend fun updateToServer() {
        todoClient.patchToServer(TodoItemsRequest("ok", list))
    }

    fun getList() = ArrayList(list)

    suspend fun add(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            list.add(todoItem)
            todoDao.insert(todoItem)
        }
    }

    suspend fun edit(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            for (i in 0 until list.size) {
                if (list[i].id == todoItem.id) {
                    list[i] = todoItem
                    break
                }
            }
            todoDao.insert(todoItem)
        }
    }

    suspend fun delete(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            list.remove(todoItem)
            todoDao.delete(todoItem)
        }
    }
}