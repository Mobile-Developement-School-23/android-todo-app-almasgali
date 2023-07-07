package com.almasgali.todoapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.almasgali.todoapp.TodoApp
import com.almasgali.todoapp.R
import com.almasgali.todoapp.data.model.TodoItem
import com.almasgali.todoapp.ui.adapter.CustomAdapter
import com.almasgali.todoapp.ui.model.TasksListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class TasksListFragment : Fragment() {

    private val viewModel: TasksListViewModel by viewModels {
        (requireContext().applicationContext as TodoApp).getComponent().viewModelsFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tasks_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val button = view.findViewById<FloatingActionButton>(R.id.add_button)
        val hideCheckbox = view.findViewById<CheckBox>(R.id.hide_checkbox)

        val onEditClickedCallback: (TodoItem) -> Unit = {
            viewModel.onEditClicked(it)
            findNavController().navigate(R.id.toAddOrEditTaskFragment)
        }

        val onHideClickedCallback: (TodoItem) -> Unit = {
            viewModel.onHideItem(it)
        }

        val adapter = CustomAdapter(onEditClickedCallback, onHideClickedCallback)

        lifecycleScope.launch {
            viewModel.uiState.collect {
                adapter.setData(it.data)
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        button.setOnClickListener {
            findNavController().navigate(R.id.toAddOrEditTaskFragment)
        }

        hideCheckbox.setOnClickListener {
            viewModel.onHideClicked(hideCheckbox.isChecked)
        }
    }
}
