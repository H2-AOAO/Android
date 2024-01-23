package kr.sesac.aoao.android.calendar.todo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.model.TodoFolderData

/**
 * @since 2024.01.23
 * @author 김유빈
 */
class RecyclerViewAdapter_Todo(
    private val folders: List<TodoFolderData>,
    private val context: TodolistFragment
)
    : RecyclerView.Adapter<RecyclerViewAdapter_Todo.TodoFolderViewHolder>()
{
    class TodoFolderViewHolder(folder: View) : RecyclerView.ViewHolder(folder) {
        val addTodoButton: Button = folder.findViewById(R.id.addTodoButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoFolderViewHolder {
        val folder = LayoutInflater.from(parent.context).inflate(R.layout.recycler_todo_item, parent, false)
        return TodoFolderViewHolder(folder)
    }

    override fun getItemCount(): Int {
        return folders.size
    }

    override fun onBindViewHolder(holder: TodoFolderViewHolder, position: Int) {
        val folder = folders[position]
        holder.addTodoButton.text = folder.name
    }
}
