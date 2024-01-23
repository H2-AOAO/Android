package kr.sesac.aoao.android.calendar.todo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.model.TodoData
import kr.sesac.aoao.android.model.TodoFolderData

/**
 * @since 2024.01.23
 * @author 김유빈
 */
class RecyclerViewAdapter_TodoFolder(
    private val folders: List<TodoFolderData>,
    private val context: TodolistFragment,
    private val onAddEvent: (MutableList<TodoData>) -> Unit,
    private val onShowEvent: (MutableList<TodoData>, TodoData) -> Unit
)
    : RecyclerView.Adapter<RecyclerViewAdapter_TodoFolder.TodoFolderViewHolder>()
{
    class TodoFolderViewHolder(folder: View) : RecyclerView.ViewHolder(folder) {
        val addTodoButton: Button = folder.findViewById(R.id.addTodoButton)
        val recyclerView: RecyclerView = folder.findViewById(R.id.recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoFolderViewHolder {
        val folder = LayoutInflater.from(parent.context).inflate(R.layout.recycler_folder_todo_item, parent, false)
        return TodoFolderViewHolder(folder)
    }

    override fun getItemCount(): Int {
        return folders.size
    }

    override fun onBindViewHolder(holder: TodoFolderViewHolder, position: Int) {
        val folder = folders[position]
        holder.addTodoButton.text = folder.name

        val adapter = RecyclerViewAdapter_Todo(folder.colorCode, folder.todos) { clickedTodo ->
            onShowEvent(folder.todos, clickedTodo)
        }
        holder.recyclerView.adapter = adapter
        holder.recyclerView.layoutManager = LinearLayoutManager(context.requireContext())

        // 폴더 선택 시 항목 추가
        holder.addTodoButton.setOnClickListener {
            onAddEvent(folder.todos)
        }
    }
}
