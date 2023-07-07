package com.almasgali.todoapp.ui.fragments

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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.almasgali.todoapp.TodoApp
import com.almasgali.todoapp.R
import com.almasgali.todoapp.data.model.Importance
import com.almasgali.todoapp.ui.model.TasksListViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*


class AddOrEditTaskFragment : Fragment() {

    private val viewModel: TasksListViewModel by viewModels {
        (requireContext().applicationContext as TodoApp).getComponent().viewModelsFactory()
    }






    private val formatterFirst = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val formatterSecond = DateTimeFormatter.ofPattern("d.MM.yyyy")

    //TODO hardcoded for now
    private val noImportance = "basic"
    private val lowImportance = "low"
    private val highImportance = "important"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val closeButton = view.findViewById<ImageButton>(R.id.close_button)
        val saveText = view.findViewById<TextView>(R.id.save)
        val editText = view.findViewById<EditText>(R.id.edit_text)
        val deleteText = view.findViewById<TextView>(R.id.delete)
        val deleteButton = view.findViewById<ImageButton>(R.id.delete_button)
        val importanceView = view.findViewById<TextView>(R.id.importance)
        val importanceType = view.findViewById<TextView>(R.id.importance_type)
        val switchDeadline = view.findViewById<SwitchCompat>(R.id.deadline)
        val textDeadline = view.findViewById<TextView>(R.id.date)

        if (viewModel.toEdit()) {
            prepareEdit(editText, importanceType, textDeadline, deleteText, deleteButton)
        }

        saveText.setOnClickListener {
            onSaveClicked(editText, importanceType, textDeadline)
        }

        closeButton.setOnClickListener {
            viewModel.onCloseClicked()
            findNavController().navigate(R.id.toTaskListFragment)
        }

        importanceView.setOnClickListener {
            onImportanceClicked(importanceView, importanceType)
        }

        switchDeadline.setOnClickListener {
            onDeadlineClicked(switchDeadline, textDeadline)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_or_edit_task, container, false)
    }

    private fun prepareEdit(
        editText: EditText,
        importanceType: TextView,
        textDeadline: TextView,
        deleteText: TextView,
        deleteButton: ImageButton
    ) {
        editText.setText(viewModel.uiState.value.item!!.text)
        importanceType.text = when (viewModel.uiState.value.item!!.importance) {
            Importance.LOW -> lowImportance
            Importance.HIGH -> highImportance
            Importance.MEDIUM -> noImportance
        }
        if (viewModel.uiState.value.item!!.deadline != null) {
            val date: LocalDate = Instant.ofEpochMilli(viewModel.uiState.value.item!!.deadline!!)
                .atZone(ZoneId.systemDefault()).toLocalDate()
            textDeadline.text = date.format(formatterFirst)
        }
        deleteText.setOnClickListener {
            onClickDelete()
        }
        deleteButton.setOnClickListener {
            onClickDelete()
        }
    }

    private fun onSaveClicked(
        editText: EditText,
        importanceType: TextView,
        textDeadline: TextView
    ) {
        val taskText = editText.text.toString()
        if (taskText.isNotEmpty()) {
            val importance: Importance = when (importanceType.text) {
                lowImportance -> Importance.LOW
                highImportance -> Importance.HIGH
                else -> Importance.MEDIUM
            }
            var deadline: Long? = null
            if (textDeadline.text != "no") {
                deadline = try {
                    LocalDate.parse(textDeadline.text, formatterFirst).toEpochDay()
                } catch (e: DateTimeParseException) {
                    LocalDate.parse(textDeadline.text, formatterSecond).toEpochDay()
                }
            }
            viewModel.update(taskText, importance, deadline)
        }
        findNavController().navigate(R.id.toTaskListFragment)
    }

    private fun onImportanceClicked(
        importanceView: TextView?,
        importanceType: TextView
    ) {
        val menu = PopupMenu(context, importanceView)
        menu.setOnMenuItemClickListener(MenuListener(importanceType))
        menu.inflate(R.menu.importance)
        menu.show()
    }

    private fun onDeadlineClicked(
        switchDeadline: SwitchCompat,
        textDeadline: TextView
    ) {
        if (switchDeadline.isChecked) {
            val c = Calendar.getInstance()
            val y = c.get(Calendar.YEAR)
            val m = c.get(Calendar.MONTH)
            val d = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
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
        viewModel.delete()
        findNavController().navigate(R.id.toTaskListFragment)
    }
}
