package com.almasgali.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.almasgali.todoapp.R
import com.almasgali.todoapp.adapter.CustomAdapter
import com.almasgali.todoapp.model.TasksListViewModel
import com.almasgali.todoapp.util.TodoItemsRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TasksListFragment : Fragment() {

    companion object {
        fun newInstance(): TasksListFragment {
            return TasksListFragment()
        }
    }

    private val viewModel: TasksListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.tasks_list, container, false)
        val data = TodoItemsRepository.getInstance()
        val list = data.getList()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val button = view.findViewById<FloatingActionButton>(R.id.add_button)
        val hideCheckbox = view.findViewById<CheckBox>(R.id.hide_checkbox)
        if (viewModel.isHidden) {
            list.removeAll{ it.isDone }
            hideCheckbox.isChecked = true
        }
        val adapter = CustomAdapter(list, context) {
            viewModel.onEditClicked(it)
            findNavController().navigate(R.id.toAddOrEditTaskFragment)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, VERTICAL, false)
        button.setOnClickListener {
            findNavController().navigate(R.id.toAddOrEditTaskFragment)
        }
        hideCheckbox.setOnClickListener {
            if (hideCheckbox.isChecked) {
                adapter.setData(data.getList().filter { !it.isDone })
                viewModel.isHidden = true
            } else {
                adapter.setData(data.getList())
                viewModel.isHidden = false
            }
        }
        return view
    }
}