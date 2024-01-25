package kr.sesac.aoao.android.calendar.todo.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kr.sesac.aoao.android.common.ToastGenerator
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.databinding.BottomSheetDialogTodoBinding
import kr.sesac.aoao.android.databinding.FragmentTodolistBinding
import kr.sesac.aoao.android.databinding.RecyclerFolderTodoItemBinding
import kr.sesac.aoao.android.model.TodoData
import kr.sesac.aoao.android.model.TodoFolderData
import kr.sesac.aoao.android.todofolder.service.TodoRepository

/**
 * @since 2024.01.22
 * @author 김유빈
 */
class TodolistFragment : Fragment() {

    private val todoRepository = TodoRepository

    private lateinit var binding : FragmentTodolistBinding
    private lateinit var folderTodoItemBinding : RecyclerFolderTodoItemBinding
    private lateinit var adapter : RecyclerViewAdapter_TodoFolder
    private lateinit var dialog : BottomSheetDialog
    private lateinit var bottomSheetBinding : BottomSheetDialogTodoBinding

    private lateinit var accessToken: String

    private lateinit var folders : MutableList<TodoFolderData>

    override fun onCreateView(
        inflater: LayoutInflater, // 뷰를 생성하는 객체
        container: ViewGroup?, // 생성할 뷰(자식 뷰)가 들어갈 부모 뷰
        savedInstanceState: Bundle? // 이전 프래그먼트 객체에서 전달된 데이터(번들)
    ): View {
        binding = FragmentTodolistBinding.inflate(layoutInflater)
        folderTodoItemBinding = RecyclerFolderTodoItemBinding.inflate(layoutInflater)

        accessToken = TokenManager.getAccessTokenWithTokenType(requireContext())

        Log.d("token", accessToken)
        setTodos("2024-01-21")

        return binding.root
    }

    /**
     * 투두 리스트 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    private fun setTodos(date: String) {
        todoRepository.findAll(accessToken, date, requireActivity(),
            onResponse = { response ->
                if (response.success && response.date != null) {
                    folders = response.date!!.convertToData()
                    setRecyclerView()
                }
            },
            onFailure = {
            })
    }

    /**
     * 투두리스트 리사이클러뷰 구현
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun setRecyclerView() {
        val recyclerView = binding.recyclerView

        adapter = RecyclerViewAdapter_TodoFolder(
            folders, this,
            { folder -> saveTodo(folder, TodoData.save()) },
            { todo -> checkTodo(todo) },
            { todos, todo -> showBottomSheetDialog(todos, todo) }
        )
        recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    /**
     * 투두 추가 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    private fun saveTodo(
        folder: TodoFolderData,
        newTodo: TodoData,
    ) {
        folder.id?.let {
            todoRepository.save(
                accessToken, it, newTodo, requireActivity(),
                onResponse = { response ->
                    if (response.success) {
                        folder.todos.add(newTodo)
                        adapter.notifyDataSetChanged()
                    }
                },
                onFailure = {
                    ToastGenerator.showShortToast("투두 저장에 실패하였습니다", requireActivity())
                })
        }
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
