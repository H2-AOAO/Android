package kr.sesac.aoao.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kr.sesac.aoao.android.databinding.ActivityTodoFolderBinding
import kr.sesac.aoao.android.model.TodoFolderData

class TodoFolderActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTodoFolderBinding
    private lateinit var folders : List<TodoFolderData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRecyclerView()
    }

    private fun setRecyclerView() {
        val recyclerView = binding.recyclerView
        folders = mutableListOf(
            TodoFolderData("공부"),
            TodoFolderData("루틴"),
            TodoFolderData("생활"),
        )
        val adapter = RecyclerViewAdapter_TodoFolder(folders, this)
        recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
