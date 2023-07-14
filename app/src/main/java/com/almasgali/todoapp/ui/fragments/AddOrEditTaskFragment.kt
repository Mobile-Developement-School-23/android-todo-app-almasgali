package com.almasgali.todoapp.ui.fragments

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.almasgali.todoapp.R
import com.almasgali.todoapp.TodoApp
import com.almasgali.todoapp.data.model.Importance
import com.almasgali.todoapp.data.model.TodoItem
import com.almasgali.todoapp.databinding.AddOrEditTaskBinding
import com.almasgali.todoapp.ui.activity.MainActivity
import com.almasgali.todoapp.ui.model.TasksListViewModel
import com.almasgali.todoapp.util.notifications.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.*


class AddOrEditTaskFragment : Fragment() {

    private val viewModel: TasksListViewModel by activityViewModels {
        (requireContext().applicationContext as TodoApp).getComponent().viewModelsFactory()
    }

    private val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    private lateinit var binding: AddOrEditTaskBinding
    private lateinit var item: TodoItem
    private var edit = false

    //TODO hardcoded for now
    private val noImportance = "basic"
    private val lowImportance = "low"
    private val highImportance = "important"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddOrEditTaskBinding.inflate(inflater, container, false)
        setFragmentResultListener("listFragment") { _, bundle ->
            prepareEdit(bundle)
        }
        setupListeners()
        return binding.root
    }

    private fun prepareEdit(bundle: Bundle) {
        val todoItem: TodoItem? =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("todo", TodoItem::class.java)
            } else
                @Suppress("DEPRECATION")
                bundle.getParcelable("todo")
        if (todoItem != null) {
            edit = true
            item = todoItem
            binding.editText.setText(todoItem.text)
            binding.importanceType.text = when (todoItem.importance) {
                Importance.LOW -> lowImportance
                Importance.HIGH -> highImportance
                Importance.MEDIUM -> noImportance
            }
            if (todoItem.deadline != null) {
                val date = Date(todoItem.deadline)
                binding.date.text = formatter.format(date)
                binding.deadline.isChecked = true
            }
            binding.delete.setOnClickListener {
                onClickDelete(todoItem)
            }
            binding.deleteButton.setOnClickListener {
                onClickDelete(todoItem)
            }
        }
    }

    private fun setupListeners() {
        binding.save.setOnClickListener {
            onSaveClicked()
        }

        binding.closeButton.setOnClickListener {
            findNavController().navigate(R.id.toTaskListFragment)
        }

        binding.importance.setOnClickListener {
            onImportanceClicked()
        }

        binding.deadline.setOnClickListener {
            onDeadlineClicked()
        }
    }

    private fun onSaveClicked() {
        val taskText = binding.editText.text.toString()
        if (taskText.isNotEmpty()) {
            val importance: Importance = when (binding.importanceType.text) {
                lowImportance -> Importance.LOW
                highImportance -> Importance.HIGH
                else -> Importance.MEDIUM
            }
            var deadline: Long? = null
            if (binding.date.text != "no") {
                deadline = formatter.parse(binding.date.text.toString())?.time
            }
            if (edit) {
                val item = TodoItem(
                    item.id,
                    taskText,
                    importance,
                    deadline,
                    false,
                    null,
                    item.created,
                    System.currentTimeMillis(),
                    item.lastUpdatedBy
                )
                if (deadline != null) {
                    setAlarm(item)
                }
                viewModel.edit(item)
            } else {
                val time = System.currentTimeMillis()
                val item = TodoItem(
                    UUID.randomUUID().toString(),
                    taskText,
                    importance,
                    deadline,
                    false,
                    null,
                    time,
                    time,
                    "pixel2"
                )
                if (deadline != null) {
                    setAlarm(item)
                }
                viewModel.add(item)
            }
        }
        findNavController().navigate(R.id.toTaskListFragment)
    }

    private fun setAlarm(item: TodoItem) {
        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.putExtra("item", item)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            UUID.fromString(item.id).hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val mainActivityIntent = Intent(requireContext(), MainActivity::class.java)
        val basicPendingIntent = PendingIntent.getActivity(
            requireContext(),
            UUID.fromString(item.id).hashCode(),
            mainActivityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val clockInfo = AlarmManager.AlarmClockInfo(item.deadline!!.toLong(), basicPendingIntent)
        alarmManager.setAlarmClock(clockInfo, pendingIntent)
    }

    private fun onImportanceClicked() {
        val menu = PopupMenu(context, binding.importance)
        menu.setOnMenuItemClickListener(MenuListener())
        menu.inflate(R.menu.importance)
        menu.show()
    }

    private fun onDeadlineClicked() {
        if (binding.deadline.isChecked) {
            val c = Calendar.getInstance()
            val y = c.get(Calendar.YEAR)
            val m = c.get(Calendar.MONTH)
            val d = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    binding.date.text =
                        (buildString {
                            append(dayOfMonth.toString())
                            append(".")
                            append(if ((monthOfYear + 1) < 10) "0" else "")
                            append((monthOfYear + 1))
                            append(".")
                            append(year)
                        })
                }, y, m, d
            )
            datePickerDialog.show()
        } else {
            binding.date.text = noImportance
        }
    }

    private inner class MenuListener : PopupMenu.OnMenuItemClickListener {

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            if (item == null) {
                return false
            }
            return when (item.itemId) {
                R.id.medium -> {
                    binding.importanceType.text = noImportance
                    true
                }
                R.id.low -> {
                    binding.importanceType.text = lowImportance
                    true
                }
                R.id.high -> {
                    binding.importanceType.text = highImportance
                    true
                }
                else -> false
            }
        }

    }

    private fun onClickDelete(todoItem: TodoItem) {
        viewModel.delete(todoItem)
        findNavController().navigate(R.id.toTaskListFragment)
    }
}
