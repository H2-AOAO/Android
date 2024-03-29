package kr.sesac.aoao.android.calendar.todo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.model.Palette
import kr.sesac.aoao.android.model.PaletteData
import kr.sesac.aoao.android.model.TodoData

/**
 * @since 2024.01.23
 * @author 김유빈
 */
class RecyclerViewAdapter_Todo(
    private val palette: PaletteData,
    private val todos: List<TodoData>,
    private val onCheckEvent: (TodoData) -> Unit,
    private val onItemClick: (TodoData) -> Unit,
)
    : RecyclerView.Adapter<RecyclerViewAdapter_Todo.TodoFolderViewHolder>()
{
    class TodoFolderViewHolder(folder: View) : RecyclerView.ViewHolder(folder) {
        val content: TextView = folder.findViewById(R.id.todoContent)
        val checked: ImageView = folder.findViewById(R.id.todoCheckIcon)
        val item: ConstraintLayout = folder.findViewById(R.id.todoItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoFolderViewHolder {
        val folder = LayoutInflater.from(parent.context).inflate(R.layout.recycler_todo_item, parent, false)
        return TodoFolderViewHolder(folder)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: TodoFolderViewHolder, position: Int) {
        val todo = todos[position]
        holder.content.text = todo.content

        // 아이템 클릭 이벤트 처리
        holder.item.setOnClickListener {
            onItemClick(todo)
        }

        // 체크 이벤트 처리
        holder.checked.setOnClickListener {
            onCheckEvent(todo)
        }

        if (!todo.checked) {
            return
        }

        holder.checked.setImageResource(Palette.from(palette.colorCode).layoutId)
    }
}
