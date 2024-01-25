package kr.sesac.aoao.android.todofolder.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kr.sesac.aoao.android.common.ToastGenerator
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.databinding.ActivityTodoFolderBinding
import kr.sesac.aoao.android.databinding.BottomSheetDialogTodoBinding
import kr.sesac.aoao.android.model.TodoFolderData
import kr.sesac.aoao.android.todofolder.model.response.FolderQueryDetailResponse
import kr.sesac.aoao.android.todofolder.service.PaletteRepository
import kr.sesac.aoao.android.todofolder.service.TodoFolderRepository

/**
 * @since 2024.01.23
 * @author 김유빈
 */
class TodoFolderActivity : AppCompatActivity() {

    private val todoFolderRepository = TodoFolderRepository
    private val paletteRepository = PaletteRepository

    private lateinit var binding : ActivityTodoFolderBinding
    private lateinit var bottomSheetBinding : BottomSheetDialogTodoBinding
    private lateinit var adapter : RecyclerViewAdapter_Folder
    private lateinit var dialog : BottomSheetDialog

    private var folders : MutableList<TodoFolderData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoFolderBinding.inflate(layoutInflater)

        val accessToken = TokenManager.getAccessTokenWithTokenType(this)
        setFolders(accessToken, "2024-01-21")
        setAddButtonClickEvent(accessToken)

        setContentView(binding.root)
    }

    /**
     * 폴더 리스트 API 호출하여 리사이클러뷰에 표출
     * @since 2024.01.25
     * @author 김유빈
     */
    private fun setFolders(accessToken: String, date: String) {
        todoFolderRepository.findAll(accessToken, date, this@TodoFolderActivity,
            onResponse = { response ->
                if (response.success && response.date != null) {
                    folders = convertToTodoFolderData(response)
                    setRecyclerView()
                }
            },
            onFailure = {
            })
    }

    /**
     * 투두리스트 폴더 리사이클러뷰 구현
     * @since 2024.01.25
     * @author 김유빈
     */
    private fun convertToTodoFolderData(response: ApplicationResponse<FolderQueryDetailResponse>) =
        response.date!!.folders
            .map { folder ->
                TodoFolderData(
                    folder.folderId,
                    folder.content,
                    folder.colorCode,
                    mutableListOf()
                )
            }
            .toMutableList()

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
    private fun setAddButtonClickEvent(accessToken: String) {
        binding.addButton.setOnClickListener {
            val folderName = "New Folder"

            // 새로운 항목 추가
            val newFolder = TodoFolderData(1, folderName, "blue", mutableListOf())
            saveFolder(accessToken, newFolder)

            // 리사이클러뷰의 마지막 항목으로 스크롤
            binding.recyclerView.scrollToPosition(folders.size - 1)
        }
    }

    /**
     * 투두리스트 폴더 추가 API 호출
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun saveFolder(
        accessToken: String,
        newFolder: TodoFolderData
    ) {
        todoFolderRepository.save(
            accessToken, newFolder, "2024-01-21", 1L, this,
            onResponse = { response ->
                if (response.success) {
                    folders.add(newFolder)
                    binding.recyclerView.adapter?.notifyItemInserted(folders.size - 1)
                }
            },
            onFailure = {
                ToastGenerator.showShortToast("폴더 저장에 실패하였습니다", this)
            })
    }

    /**
     * 투두리스트 폴더 수정 및 삭제 다이얼로그 구현
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun showBottomSheetDialog(clickedFolder: TodoFolderData) {
        dialog = BottomSheetDialog(this)
        bottomSheetBinding = BottomSheetDialogTodoBinding.inflate(layoutInflater)
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
