package kr.sesac.aoao.android.todofolder.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.model.TodoFolderData

class RecyclerViewAdapter_TodoFolder(
    private val folders: List<TodoFolderData>,
    private val context: Context,
    private val onItemClick: (TodoFolderData) -> Unit)
    : RecyclerView.Adapter<RecyclerViewAdapter_TodoFolder.TodoFolderViewHolder>()
{
    class TodoFolderViewHolder(folder: View) : RecyclerView.ViewHolder(folder) {
        val name: TextView = folder.findViewById(R.id.todoFolderName)
        val item: ConstraintLayout = folder.findViewById(R.id.todoFolderItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoFolderViewHolder {
        val folder = LayoutInflater.from(parent.context).inflate(R.layout.recycler_todo_folder_item, parent, false)
        return TodoFolderViewHolder(folder)
    }

    override fun getItemCount(): Int {
        return folders.size
    }

    override fun onBindViewHolder(holder: TodoFolderViewHolder, position: Int) {
        val folder = folders[position]
        holder.name.text = folder.name

        // 아이템 클릭 이벤트 처리
        holder.item.setOnClickListener {
            onItemClick(folder)
        }
    }
}
