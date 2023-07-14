package com.almasgali.todoapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.almasgali.todoapp.R
import com.almasgali.todoapp.TodoApp
import com.almasgali.todoapp.data.model.TodoItem
import com.almasgali.todoapp.ui.adapter.CustomAdapter
import com.almasgali.todoapp.ui.model.TasksListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import com.google.android.material.snackbar.Snackbar

class TasksListFragment : Fragment() {

    private val viewModel: TasksListViewModel by activityViewModels {
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
            setFragmentResult("listFragment", bundleOf("todo" to it))
            findNavController().navigate(R.id.toAddOrEditTaskFragment)
        }

        val onHideClickedCallback: (TodoItem) -> Unit = {
            viewModel.onHideItem(it)
        }

        val adapter = CustomAdapter(onEditClickedCallback, onHideClickedCallback)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (hideCheckbox.isChecked) {
                        adapter.setData(it.data.filter { item -> !item.isDone })
                    } else {
                        adapter.setData(it.data)
                    }
                }
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        if (viewModel.deleted) {
            Snackbar.make(recyclerView, "Restore ${viewModel.deletedItem.text}?", Snackbar.LENGTH_LONG)
                .setAction("Restore") {
                    viewModel.add(viewModel.deletedItem)
                    viewModel.deleted = false
                }.show()
        }

        button.setOnClickListener {
            findNavController().navigate(R.id.toAddOrEditTaskFragment)
        }
    }
}
