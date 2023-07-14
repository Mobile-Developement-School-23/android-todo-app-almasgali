package com.almasgali.todoapp.di.module

import com.almasgali.todoapp.api.services.TodoService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RemoteModule {

    companion object Constants {
        const val TOKEN = "misbeginning"
        const val BASEURL = "https://beta.mrdekk.ru/todobackend/"
    }

    @Provides
    fun provideTodoService(okHttpClient: OkHttpClient): TodoService =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoService::class.java)

    @Provides
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(interceptor).build()

    @Provides
    fun provideInterceptor(): Interceptor =
        Interceptor() { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $TOKEN")
                .build()
            chain.proceed(request)
        }
}
