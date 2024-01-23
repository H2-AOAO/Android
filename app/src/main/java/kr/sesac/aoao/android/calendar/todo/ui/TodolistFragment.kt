package kr.sesac.aoao.android.calendar.todo.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kr.sesac.aoao.android.databinding.FragmentTodolistBinding
import kr.sesac.aoao.android.model.TodoFolderData
import kr.sesac.aoao.android.model.TodoFoldersData

/**
 * @since 2024.01.22
 * @author 김유빈
 */
class TodolistFragment : Fragment() {

    private lateinit var binding : FragmentTodolistBinding
    private lateinit var adapter : RecyclerViewAdapter_TodoFolder

    private lateinit var folders : MutableList<TodoFolderData>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, // 뷰를 생성하는 객체
        container: ViewGroup?, // 생성할 뷰(자식 뷰)가 들어갈 부모 뷰
        savedInstanceState: Bundle? // 이전 프래그먼트 객체에서 전달된 데이터(번들)
    ): View {
        binding = FragmentTodolistBinding.inflate(layoutInflater)

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

        adapter = RecyclerViewAdapter_TodoFolder(folders, this)
        recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
