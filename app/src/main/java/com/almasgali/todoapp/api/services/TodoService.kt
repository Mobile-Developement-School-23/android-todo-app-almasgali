package com.almasgali.todoapp.api.services

import com.almasgali.todoapp.api.data.TodoItemsRequest
import com.almasgali.todoapp.api.data.TodoItemsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface TodoService {

    @GET("list")
    suspend fun getList(): Response<TodoItemsResponse>

    @PATCH("list")
    suspend fun patchList(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body todoBody: TodoItemsRequest
    ): Response<TodoItemsResponse>
}