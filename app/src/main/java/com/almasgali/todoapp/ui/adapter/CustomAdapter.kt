package com.almasgali.todoapp.ui.adapter


import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.almasgali.todoapp.R
import com.almasgali.todoapp.data.model.Importance
import com.almasgali.todoapp.data.model.TodoItem
import java.text.SimpleDateFormat
import java.util.*

class CustomAdapter(
    private val onEditClicked: (TodoItem) -> Unit,
    private val onHideClicked: (TodoItem) -> Unit
) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private var data: List<TodoItem> = listOf()

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val checkBox = view.findViewById<CheckBox>(R.id.complete_check_box)
        private val taskText = view.findViewById<TextView>(R.id.task_text)
        private val date = view.findViewById<TextView>(R.id.date)
        private val info = view.findViewById<ImageButton>(R.id.info)
        private val priority = view.findViewById<ImageView>(R.id.priority)
        private val context = view.context

        private val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        fun bind(todoItem: TodoItem) {
            taskText.text = todoItem.text
            checkBox.isChecked = todoItem.isDone
            if (todoItem.deadline != null) {
                val deadlineDate = Date(todoItem.deadline)
                date.text = formatter.format(deadlineDate)
                date.visibility = View.VISIBLE
            } else {
                date.visibility = View.INVISIBLE
            }
            when (todoItem.importance) {
                Importance.LOW -> {
                    priority.visibility = View.VISIBLE
                    priority.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.low_priority
                        )
                    )
                }
                Importance.MEDIUM -> {
                    priority.visibility = View.GONE
                }
                Importance.HIGH -> {
                    priority.visibility = View.VISIBLE
                    priority.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.high_priority
                        )
                    )
                }
            }
            info.setOnClickListener {
                onEditClicked(todoItem)
            }
            taskText.setOnClickListener {
                onEditClicked(todoItem)
            }
            checkBox.setOnClickListener {
                onHideClicked(todoItem)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.task, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(data[position])
    }

    fun setData(newData: List<TodoItem>) {
        val callback = CustomCallback(data, newData)
        val diff = DiffUtil.calculateDiff(callback)
        data = newData
        diff.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = data.size

    class CustomCallback(
        private val oldData: List<TodoItem>,
        private val newData: List<TodoItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldData.size

        override fun getNewListSize(): Int = newData.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldData[oldItemPosition].id == newData[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldData[oldItemPosition] == newData[newItemPosition]
        }
    }
}
