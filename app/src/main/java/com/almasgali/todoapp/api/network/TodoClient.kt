package com.almasgali.todoapp.api.network

import com.almasgali.todoapp.api.data.StatusCode
import com.almasgali.todoapp.api.data.TodoItemsRequest
import com.almasgali.todoapp.api.services.TodoService
import com.almasgali.todoapp.data.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TodoClient {

    companion object Constants {
        const val TOKEN = "misbeginning"
        const val BASEURL = "https://beta.mrdekk.ru/todobackend/"
    }

    suspend fun patchToServer(todoItemsRequest: TodoItemsRequest): StatusCode =
        withContext(Dispatchers.IO) {
            val response = retrofit.patchList(1, todoItemsRequest)
            when {
                response.code() in 200..299 -> {
                    StatusCode.SUCCESS
                }
                else -> StatusCode.FAIL
            }
        }

    suspend fun getFromServer(): List<TodoItem> =
        withContext(Dispatchers.IO) {
            val response = retrofit.getList()
            when {
                response.code() in 200..299 && response.body() != null-> {
                    response.body()!!.list
                }
                else -> emptyList()
            }
        }

    private val interceptor = Interceptor() { chain ->
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorisation", "Bearer $TOKEN")
            .build()
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TodoService::class.java)
}