package kr.sesac.aoao.android.calendar.todo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kr.sesac.aoao.android.common.ToastGenerator
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.databinding.BottomSheetDialogTodoBinding
import kr.sesac.aoao.android.databinding.FragmentTodolistBinding
import kr.sesac.aoao.android.databinding.RecyclerFolderTodoItemBinding
import kr.sesac.aoao.android.model.TodayViewModel
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
    private lateinit var todayViewModel: TodayViewModel
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
        todayViewModel = ViewModelProvider(requireActivity())[TodayViewModel::class.java]

        accessToken = TokenManager.getAccessTokenWithTokenType(requireContext())
        observeSelectedDate()

        return binding.root
    }

    /**
     * 화면에 접근했을 때 투두리스트 화면 데이터 재표출
     * @since 2024.01.27
     * @author 김유빈
     */
    override fun onResume() {
        super.onResume()
        observeSelectedDate()
    }

    /**
     * 캘린더 정보 읽어와 해당 일자에 맞는 투두리스트 표출
     * @since 2024.01.25
     * @author 김유빈
     */
    private fun observeSelectedDate() {
        todayViewModel.today.observe(viewLifecycleOwner) { date ->
            val today = "${date.year}-${date.month}-${date.dayOfMonth}"
            setTodos(today)
        }
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
            { folder, todo -> checkTodo(folder, todo) },
            { folder, todo -> showBottomSheetDialog(folder, todo) }
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
        todoRepository.save(
            accessToken, folder.id, newTodo, requireActivity(),
            onResponse = { response ->
                if (response.success && response.date != null) {
                    newTodo.id = response.date!!.todoId
                    folder.todos.add(newTodo)
                    adapter.notifyDataSetChanged()
                }
            },
            onFailure = {
                ToastGenerator.showShortToast("투두 저장에 실패하였습니다", requireActivity())
            })
    }

    /**
     * 투두리스트 항목 체크
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun checkTodo(folder: TodoFolderData, todo: TodoData) {
        when (todo.checked) {
            true -> uncheckTodo(folder.id, todo.id)
            false -> checkTodo(folder.id, todo.id)
        }
        todo.checked = !todo.checked
        adapter.notifyDataSetChanged()
    }

    /**
     * 투두 체크 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    private fun checkTodo(
        folderId: Long?,
        todoId: Long?,
    ) {
        todoRepository.check(
            accessToken, folderId, todoId, requireActivity(),
            onResponse = { response ->
            },
            onFailure = {
                ToastGenerator.showShortToast("투두 체크에 실패하였습니다", requireActivity())
            }
        )
    }

    /**
     * 투두 체크 체크 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    private fun uncheckTodo(
        folderId: Long?,
        todoId: Long?,
    ) {
        todoRepository.uncheck(
            accessToken, folderId, todoId, requireActivity(),
            onResponse = { response ->
            },
            onFailure = {
                ToastGenerator.showShortToast("투두 체크 취소에 실패하였습니다", requireActivity())
            }
        )
    }

    /**
     * 투두리스트 수정 및 삭제 다이얼로그 구현
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun showBottomSheetDialog(folder: TodoFolderData, todo: TodoData) {
        dialog = BottomSheetDialog(requireContext())
        bottomSheetBinding = BottomSheetDialogTodoBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.bottomSheetTitle.setText(todo.content)
        setUpdateButtonEventInBottomSheetDialog(folder, todo)
        setDeleteButtonEventInBottomSheetDialog(folder, todo)

        dialog.show()
    }

    /**
     * 투두리스트 수정 버튼 이벤트 구현
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun setUpdateButtonEventInBottomSheetDialog(folder: TodoFolderData, todo: TodoData) {
        bottomSheetBinding.updateButton.setOnClickListener {
            todo.content = bottomSheetBinding.bottomSheetTitle.text.toString()
            updateTodo(folder.id, todo)
            dialog.dismiss()
        }
    }

    /**
     * 투두 수정 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    private fun updateTodo(
        folderId: Long?,
        todo: TodoData,
    ) {
        todoRepository.update(
            accessToken, folderId, todo, requireActivity(),
            onResponse = { response ->
                adapter.notifyDataSetChanged()
            },
            onFailure = {
                ToastGenerator.showShortToast("투두 수정에 실패하였습니다", requireActivity())
            }
        )
    }

    /**
     * 투두리스트 삭제 버튼 이벤트 구현
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun setDeleteButtonEventInBottomSheetDialog(folder: TodoFolderData, todo: TodoData) {
        bottomSheetBinding.deleteButton.setOnClickListener {
            folder.todos.remove(todo)
            deleteTodo(folder.id, todo.id)
            dialog.dismiss()
        }
    }

    /**
     * 투두 삭제 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    private fun deleteTodo(
        folderId: Long?,
        todoId: Long?,
    ) {
        todoRepository.delete(
            accessToken, folderId, todoId, requireActivity(),
            onResponse = { response ->
                adapter.notifyDataSetChanged()
            },
            onFailure = {
                ToastGenerator.showShortToast("투두 삭제에 실패하였습니다", requireActivity())
            }
        )
    }
}
