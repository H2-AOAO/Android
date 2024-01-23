package kr.sesac.aoao.android.todofolder.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kr.sesac.aoao.android.databinding.ActivityTodoFolderBinding
import kr.sesac.aoao.android.databinding.BottomSheetDialogTodoFolderBinding
import kr.sesac.aoao.android.model.TodoFolderData
import kr.sesac.aoao.android.model.TodoFoldersData

/**
 * @since 2024.01.23
 * @author 김유빈
 */
class TodoFolderActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTodoFolderBinding
    private lateinit var bottomSheetBinding : BottomSheetDialogTodoFolderBinding
    private lateinit var adapter : RecyclerViewAdapter_Folder
    private lateinit var dialog : BottomSheetDialog

    private lateinit var folders : MutableList<TodoFolderData>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        folders = intent.getParcelableExtra("folders", TodoFoldersData::class.java)!!.data

        setRecyclerView()
        setAddButtonClickEvent()
    }

    /**
     * 투두리스트 폴더 리사이클러뷰 구현
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun setRecyclerView() {
        val recyclerView = binding.recyclerView
        adapter = RecyclerViewAdapter_Folder(folders, this) { clickedFolder ->
            showBottomSheetDialog(clickedFolder)
        }
        recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    /**
     * 투두리스트 폴더 추가 버튼 이벤트 구현
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun setAddButtonClickEvent() {
        binding.addButton.setOnClickListener {
            val folderName = "New Folder"

            // 새로운 항목 추가
            val newFolder = TodoFolderData(folderName, null, "blue")
            folders.add(newFolder)
            binding.recyclerView.adapter?.notifyItemInserted(folders.size - 1)

            // 리사이클러뷰의 마지막 항목으로 스크롤
            binding.recyclerView.scrollToPosition(folders.size - 1)
        }
    }

    /**
     * 투두리스트 폴더 수정 및 삭제 다이얼로그 구현
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun showBottomSheetDialog(clickedFolder: TodoFolderData) {
        dialog = BottomSheetDialog(this)
        bottomSheetBinding = BottomSheetDialogTodoFolderBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.bottomSheetTitle.setText(clickedFolder.name)
        setUpdateButtonEventInBottomSheetDialog(clickedFolder)
        setDeleteButtonEventInBottomSheetDialog(clickedFolder)

        dialog.show()
    }

    /**
     * 투두리스트 폴더 수정 버튼 이벤트 구현
     * @since 2024.01.23
     * @author 김유빈
     */
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

    /**
     * 투두리스트 폴더 삭제 버튼 이벤트 구현
     * @since 2024.01.23
     * @author 김유빈
     */
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
