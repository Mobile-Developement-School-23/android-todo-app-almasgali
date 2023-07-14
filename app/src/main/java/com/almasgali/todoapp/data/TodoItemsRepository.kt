package com.almasgali.todoapp.data

import com.almasgali.todoapp.api.data.RemoteDataSource
import com.almasgali.todoapp.data.model.TodoItem
import com.almasgali.todoapp.util.StatusCode
import com.almasgali.todoapp.util.fromRemoteToLocal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoItemsRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

    companion object Constants {
        val OK_CODES = 200..299
    }

    suspend fun updateFromServer(): StatusCode {
        val response = remoteDataSource.getTodoList()
        if (response.code() in OK_CODES) {
            localDataSource.insertTodoList(response.body()!!.list.map { fromRemoteToLocal(it) })
            remoteDataSource.setRevision(response.body()!!.revision)
            return StatusCode.SUCCESS
        }
        return StatusCode.FAIL
    }

    suspend fun updateToServer(): StatusCode {
        val response = remoteDataSource.patchTodoList(localDataSource.getTodoList())
        if (response.code() in OK_CODES) {
            remoteDataSource.setRevision(response.body()!!.revision)
            return StatusCode.SUCCESS
        }
        return StatusCode.FAIL
    }

    fun getListFlow() = localDataSource.getTodoListFlow()

    fun getList() = localDataSource.getTodoList()

    suspend fun add(todoItem: TodoItem) {
        localDataSource.insertTodoItem(todoItem)
        val response = remoteDataSource.postTodoItem(todoItem)
        if (response.code() in OK_CODES) {
            remoteDataSource.setRevision(response.body()!!.revision)
        }
    }

    suspend fun edit(todoItem: TodoItem) {
        localDataSource.insertTodoItem(todoItem)
        val response = remoteDataSource.putTodoItem(todoItem)
        if (response.code() in OK_CODES) {
            remoteDataSource.setRevision(response.body()!!.revision)
        }
    }

    suspend fun delete(todoItem: TodoItem) {
        localDataSource.deleteTodoItem(todoItem)
        val response = remoteDataSource.deleteTodoItem(todoItem)
        if (response.code() in OK_CODES) {
            remoteDataSource.setRevision(response.body()!!.revision)
        }
    }
}