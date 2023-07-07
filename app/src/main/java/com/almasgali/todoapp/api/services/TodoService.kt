package com.almasgali.todoapp.api.services

import com.almasgali.todoapp.api.data.model.TodoItemRequest
import com.almasgali.todoapp.api.data.model.TodoItemResponse
import com.almasgali.todoapp.api.data.model.TodoItemsRequest
import com.almasgali.todoapp.api.data.model.TodoItemsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.Body
import retrofit2.http.Header

interface TodoService {

    companion object Constants {
        const val REVISION_HEADER = "X-Last-Known-Revision"
    }

    @GET("list")
    suspend fun getList(): Response<TodoItemsResponse>

    @PATCH("list")
    suspend fun patchList(
        @Header(REVISION_HEADER) revision: Int,
        @Body todoBody: TodoItemsRequest
    ): Response<TodoItemsResponse>

    @PUT("list/{id}")
    suspend fun putItem(
        @Header(REVISION_HEADER) revision: Int,
        @Path("id") id: String,
        @Body todoBody: TodoItemRequest,
    ): Response<TodoItemResponse>

    @POST("list")
    suspend fun postItem(
        @Header(REVISION_HEADER) revision: Int,
        @Body todoBody: TodoItemRequest,
    ): Response<TodoItemResponse>

    @DELETE("list/{id}")
    suspend fun deleteItem(
        @Header(REVISION_HEADER) revision: Int,
        @Path("id") id: String,
    ): Response<TodoItemResponse>
}
