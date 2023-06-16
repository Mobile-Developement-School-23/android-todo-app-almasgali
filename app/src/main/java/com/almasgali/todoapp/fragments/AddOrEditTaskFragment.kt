package com.almasgali.todoapp.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.almasgali.todoapp.R
import com.almasgali.todoapp.model.TasksListViewModel
import com.almasgali.todoapp.util.Importance
import com.almasgali.todoapp.util.TodoItem
import com.almasgali.todoapp.util.TodoItemsRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class AddOrEditTaskFragment : Fragment() {

    companion object {
        fun newInstance(): AddOrEditTaskFragment {
            return AddOrEditTaskFragment()
        }
    }

    private val viewModel: TasksListViewModel by activityViewModels()
    private val data = TodoItemsRepository.getInstance()
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    //TODO hardcoded for now
    private val noImportance = "no"
    private val lowImportance = "low"
    private val highImportance = "high"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_or_edit_task, container, false)
        val closeButton = view.findViewById<ImageButton>(R.id.close_button)
        val saveText = view.findViewById<TextView>(R.id.save)
        val editText = view.findViewById<EditText>(R.id.edit_text)
        val deleteText = view.findViewById<TextView>(R.id.delete)
        val deleteButton = view.findViewById<ImageButton>(R.id.delete_button)
        val importanceView = view.findViewById<TextView>(R.id.importance)
        val importanceType = view.findViewById<TextView>(R.id.importance_type)
        val switchDeadline = view.findViewById<SwitchCompat>(R.id.deadline)
        val textDeadline = view.findViewById<TextView>(R.id.date)

        if (viewModel.toEdit) {
            editText.setText(viewModel.todoItem!!.text)
            importanceType.text = when (viewModel.todoItem!!.importance) {
                Importance.LOW -> lowImportance
                Importance.HIGH -> highImportance
                Importance.MEDIUM -> noImportance
            }
            if (viewModel.todoItem!!.deadline != null) {
                textDeadline.text = viewModel.todoItem!!.deadline!!.format(formatter)
            }
            deleteText.setOnClickListener {
                onClickDelete()
            }
            deleteButton.setOnClickListener {
                onClickDelete()
            }
        }

        saveText.setOnClickListener {
            val taskText = editText.text.toString()
            if (taskText.isNotEmpty()) {
                val importance: Importance = when (importanceType.text) {
                    lowImportance -> Importance.LOW
                    highImportance -> Importance.HIGH
                    else -> Importance.MEDIUM
                }
                var deadline: LocalDate? = null
                if (textDeadline.text != "no") {
                    deadline = LocalDate.parse(textDeadline.text, formatter)
                }
                if (viewModel.toEdit) {
                    viewModel.toEdit = false
                    data.edit(
                        TodoItem(
                            viewModel.todoItem!!.id,
                            taskText,
                            importance,
                            deadline,
                            viewModel.todoItem!!.isDone,
                            viewModel.todoItem!!.created,
                            LocalDate.now()
                        )
                    )
                } else {
                    data.add(
                        TodoItem(
                            data.getId(),
                            taskText,
                            importance,
                            deadline,
                            false,
                            LocalDate.now(),
                            null
                        )
                    )
                }
            }
            findNavController().navigate(R.id.toTaskListFragment)
        }
        closeButton.setOnClickListener {
            findNavController().navigate(R.id.toTaskListFragment)
        }
        importanceView.setOnClickListener {
            val menu = PopupMenu(context, importanceView)
            menu.setOnMenuItemClickListener(MenuListener(importanceType))
            menu.inflate(R.menu.importance)
            menu.show()
        }
        switchDeadline.setOnClickListener {
            if (switchDeadline.isChecked) {
                val c = Calendar.getInstance()
                val y = c.get(Calendar.YEAR)
                val m = c.get(Calendar.MONTH)
                val d = c.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    context!!,
                    { _, year, monthOfYear, dayOfMonth ->
                        textDeadline.text =
                            (buildString {
                                append(dayOfMonth.toString())
                                append(".")
                                append(if ((monthOfYear + 1) < 10) "0" else "")
                                append((monthOfYear + 1))
                                append(".")
                                append(year)
                            })
                    },
                    y,
                    m,
                    d
                )
                datePickerDialog.show()
            } else {
                textDeadline.text = noImportance
            }
        }
        return view
    }

    private inner class MenuListener(private val importanceType: TextView) :
        PopupMenu.OnMenuItemClickListener {

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            if (item == null) {
                return false
            }
            return when (item.itemId) {
                R.id.medium -> {
                    importanceType.text = noImportance
                    true
                }
                R.id.low -> {
                    importanceType.text = lowImportance
                    true
                }
                R.id.high -> {
                    importanceType.text = highImportance
                    true
                }
                else -> false
            }
        }

    }

    private fun onClickDelete() {
        data.delete(viewModel.todoItem!!)
        viewModel.toEdit = false
        findNavController().navigate(R.id.toTaskListFragment)
    }
}