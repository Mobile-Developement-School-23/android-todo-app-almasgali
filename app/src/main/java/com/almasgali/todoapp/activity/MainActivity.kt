package com.almasgali.todoapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.almasgali.todoapp.R
import com.almasgali.todoapp.data.TodoItemsRepository
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch {
            val todoItemsRepository = TodoItemsRepository.getInstance(applicationContext)
            todoItemsRepository.updateFromServer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.launch {
            val todoItemsRepository = TodoItemsRepository.getInstance(applicationContext)
            todoItemsRepository.updateToServer()
        }
    }
}