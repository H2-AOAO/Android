package kr.sesac.aoao.android.calendar.todo.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kr.sesac.aoao.android.databinding.BottomSheetDialogTodoBinding
import kr.sesac.aoao.android.databinding.FragmentTodolistBinding
import kr.sesac.aoao.android.databinding.RecyclerFolderTodoItemBinding
import kr.sesac.aoao.android.model.TodoData
import kr.sesac.aoao.android.model.TodoFolderData
import kr.sesac.aoao.android.model.TodoFoldersData

/**
 * @since 2024.01.22
 * @author 김유빈
 */
class TodolistFragment : Fragment() {

    private lateinit var binding : FragmentTodolistBinding
    private lateinit var folderTodoItemBinding : RecyclerFolderTodoItemBinding
    private lateinit var adapter : RecyclerViewAdapter_TodoFolder
    private lateinit var dialog : BottomSheetDialog
    private lateinit var bottomSheetBinding : BottomSheetDialogTodoBinding

    private lateinit var folders : MutableList<TodoFolderData>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, // 뷰를 생성하는 객체
        container: ViewGroup?, // 생성할 뷰(자식 뷰)가 들어갈 부모 뷰
        savedInstanceState: Bundle? // 이전 프래그먼트 객체에서 전달된 데이터(번들)
    ): View {
        binding = FragmentTodolistBinding.inflate(layoutInflater)
        folderTodoItemBinding = RecyclerFolderTodoItemBinding.inflate(layoutInflater)

        setRecyclerView()

        return binding.root
    }

    /**
     * 투두리스트 리사이클러뷰 구현
     * @since 2024.01.23
     * @author 김유빈
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setRecyclerView() {
        val recyclerView = binding.recyclerView

        folders = arguments?.getParcelable("folders", TodoFoldersData::class.java)!!.data

        adapter = RecyclerViewAdapter_TodoFolder(
            folders, this,
            { todos -> addTodo(todos) },
            { todo -> checkTodo(todo) },
            { todos, todo -> showBottomSheetDialog(todos, todo) }
        )
        recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    /**
     * 투두리스트 항목 추가
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun addTodo(todos: MutableList<TodoData>) {
        val todoName = "New Todo"

        // 새로운 항목 추가
        val newTodo = TodoData(todoName, false)
        todos.add(newTodo)

        // 어댑터에 변경된 데이터 알려주기
        adapter.notifyDataSetChanged()
    }

    /**
     * 투두리스트 항목 체크
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun checkTodo(todo: TodoData) {
        todo.checked = !todo.checked
        adapter.notifyDataSetChanged()
    }

    /**
     * 투두리스트 수정 및 삭제 다이얼로그 구현
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun showBottomSheetDialog(todos: MutableList<TodoData>, todo: TodoData) {
        dialog = BottomSheetDialog(requireContext())
        bottomSheetBinding = BottomSheetDialogTodoBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.bottomSheetTitle.setText(todo.content)
        setUpdateButtonEventInBottomSheetDialog(todo)
        setDeleteButtonEventInBottomSheetDialog(todos, todo)

        dialog.show()
    }

    /**
     * 투두리스트 수정 버튼 이벤트 구현
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun setUpdateButtonEventInBottomSheetDialog(todo: TodoData) {
        bottomSheetBinding.updateButton.setOnClickListener {
            todo.content = bottomSheetBinding.bottomSheetTitle.text.toString()
            dialog.dismiss()
            adapter.notifyDataSetChanged()
        }
    }

    /**
     * 투두리스트 삭제 버튼 이벤트 구현
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun setDeleteButtonEventInBottomSheetDialog(todos: MutableList<TodoData>, todo: TodoData) {
        bottomSheetBinding.deleteButton.setOnClickListener {
            todos.remove(todo)
            dialog.dismiss()
            adapter.notifyDataSetChanged()
        }
    }
}
