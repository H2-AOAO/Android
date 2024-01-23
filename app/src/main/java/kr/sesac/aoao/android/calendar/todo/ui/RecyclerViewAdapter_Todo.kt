package kr.sesac.aoao.android.calendar.todo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.model.TodoData

class RecyclerViewAdapter_Todo(
    private val colorCode: String,
    private val todos: List<TodoData>
)
    : RecyclerView.Adapter<RecyclerViewAdapter_Todo.TodoFolderViewHolder>()
{
    class TodoFolderViewHolder(folder: View) : RecyclerView.ViewHolder(folder) {
        val content: TextView = folder.findViewById(R.id.todoContent)
        val checked: ImageView = folder.findViewById(R.id.todoCheckIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoFolderViewHolder {
        val folder = LayoutInflater.from(parent.context).inflate(R.layout.recycler_todo_item, parent, false)
        return TodoFolderViewHolder(folder)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: TodoFolderViewHolder, position: Int) {
        val folder = todos[position]
        holder.content.text = folder.content
        if (!folder.checked) {
            return
        }

        when (colorCode) {
            "blue" -> holder.checked.setImageResource(R.drawable.todo_checked_blue)
            "pink" -> holder.checked.setImageResource(R.drawable.todo_checked_pink)
            "yellow" -> holder.checked.setImageResource(R.drawable.todo_checked_yellow)
        }
    }
}
