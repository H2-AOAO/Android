package kr.sesac.aoao.android.todofolder.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kr.sesac.aoao.android.databinding.ActivityTodoFolderBinding
import kr.sesac.aoao.android.databinding.BottomSheetDialogTodoFolderBinding
import kr.sesac.aoao.android.model.TodoFolderData

class TodoFolderActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTodoFolderBinding
    private lateinit var bottomSheetBinding : BottomSheetDialogTodoFolderBinding
    private lateinit var adapter : RecyclerViewAdapter_TodoFolder
    private lateinit var dialog : BottomSheetDialog

    private lateinit var folders : MutableList<TodoFolderData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRecyclerView()
        setAddButtonClickEvent()
    }

    private fun setRecyclerView() {
        val recyclerView = binding.recyclerView
        folders = mutableListOf(
            TodoFolderData("공부"),
            TodoFolderData("루틴"),
            TodoFolderData("생활"),
        )
        adapter = RecyclerViewAdapter_TodoFolder(folders, this) { clickedFolder ->
            showBottomSheetDialog(clickedFolder)
        }
        recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setAddButtonClickEvent() {
        binding.addButton.setOnClickListener {
            val folderName = "New Folder"

            // 새로운 항목 추가
            val newFolder = TodoFolderData(folderName)
            folders.add(newFolder)
            binding.recyclerView.adapter?.notifyItemInserted(folders.size - 1)

            // 리사이클러뷰의 마지막 항목으로 스크롤
            binding.recyclerView.scrollToPosition(folders.size - 1)
        }
    }

    private fun showBottomSheetDialog(clickedFolder: TodoFolderData) {
        dialog = BottomSheetDialog(this)
        bottomSheetBinding = BottomSheetDialogTodoFolderBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.bottomSheetTitle.setText(clickedFolder.name)
        setUpdateButtonEventInBottomSheetDialog(clickedFolder)
        setDeleteButtonEventInBottomSheetDialog(clickedFolder)

        dialog.show()
    }

    private fun setUpdateButtonEventInBottomSheetDialog(folder: TodoFolderData) {
        bottomSheetBinding.updateButton.setOnClickListener {
            Thread {
                folder.name = bottomSheetBinding.bottomSheetTitle.text.toString()
                runOnUiThread {
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }.start()
        }
    }

    private fun setDeleteButtonEventInBottomSheetDialog(folder: TodoFolderData) {
        bottomSheetBinding.deleteButton.setOnClickListener {
            Thread {
                folders.remove(folder)
                runOnUiThread {
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }.start()
        }
    }
}
