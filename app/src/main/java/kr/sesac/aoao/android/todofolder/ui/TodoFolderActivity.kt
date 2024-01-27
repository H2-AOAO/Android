package kr.sesac.aoao.android.todofolder.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.ToastGenerator
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.databinding.ActivityTodoFolderBinding
import kr.sesac.aoao.android.databinding.BottomSheetDialogTodoFolderBinding
import kr.sesac.aoao.android.model.PaletteData
import kr.sesac.aoao.android.model.TodoFolderData
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
    private lateinit var bottomSheetBinding : BottomSheetDialogTodoFolderBinding
    private lateinit var adapter : RecyclerViewAdapter_Folder
    private lateinit var paletteAdapter: RecyclerViewAdapter_Palette
    private lateinit var dialog : BottomSheetDialog

    private lateinit var accessToken: String

    private var folders : MutableList<TodoFolderData> = mutableListOf()
    private var palettes : MutableList<PaletteData> = mutableListOf()
    private var selectedPaletteId : Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoFolderBinding.inflate(layoutInflater)

        accessToken = TokenManager.getAccessTokenWithTokenType(this)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색으로

        val date = intent.getStringExtra("date")

        setFolders(date)
        setAddButtonClickEvent(date)

        setContentView(binding.root)
    }

    /**
     * 폴더 리스트 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    private fun setFolders(date: String?) {
        todoFolderRepository.findAll(accessToken, date, this@TodoFolderActivity,
            onResponse = { response ->
                if (response.success && response.date != null) {
                    folders = response.date!!.convertToData()
                    setTodoFolderRecyclerView()
                }
            },
            onFailure = { response ->
                ToastGenerator.showShortToast(response.message, this)
            })
    }

    /**
     * 투두리스트 폴더 리사이클러뷰 구현
     * @since 2024.01.23
     * @author 김유빈
     */
    private fun setTodoFolderRecyclerView() {
        val recyclerView = binding.recyclerView
        adapter = RecyclerViewAdapter_Folder(folders) { clickedFolder ->
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
    private fun setAddButtonClickEvent(date: String?) {
        binding.addButton.setOnClickListener {
            saveFolder(TodoFolderData.save(), date)
        }
    }

    /**
     * 투두리스트 폴더 추가 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    private fun saveFolder(
        newFolder: TodoFolderData,
        date: String?,
    ) {
        todoFolderRepository.save(
            accessToken, newFolder, date, 13L, this,
            onResponse = { response ->
                if (response.success) {
                    folders.add(newFolder)
                    binding.recyclerView.adapter?.notifyItemInserted(folders.size - 1)
                    binding.recyclerView.scrollToPosition(folders.size - 1)
                }
            },
            onFailure = { response ->
                ToastGenerator.showShortToast(response.message, this)
            })
    }

    /**
     * 투두리스트 폴더 수정 및 삭제 다이얼로그 구현
     * @since 2024.01.25
     * @author 김유빈
     */
    private fun showBottomSheetDialog(clickedFolder: TodoFolderData) {
        dialog = BottomSheetDialog(this)
        bottomSheetBinding = BottomSheetDialogTodoFolderBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.bottomSheetTitle.setText(clickedFolder.name)
        setPalettes()
        setUpdateButtonEventInBottomSheetDialog(clickedFolder)
        setDeleteButtonEventInBottomSheetDialog(clickedFolder)

        dialog.show()
    }

    /**
     * 팔레트 리스트 API 호출
     * @since 2024.01.28
     * @author 김유빈
     */
    private fun setPalettes() {
        paletteRepository.findAll(this@TodoFolderActivity,
            onResponse = { response ->
                if (response.success && response.date != null) {
                    palettes = response.date!!.convertToData()
                    setPaletteRecyclerView()
                }
            },
            onFailure = { response ->
                ToastGenerator.showShortToast(response.message, this)
            })
    }

    /**
     * 팔레트 리사이클러뷰 구현
     * @since 2024.01.28
     * @author 김유빈
     */
    private fun setPaletteRecyclerView() {
        val recyclerView = bottomSheetBinding.paletteRecyclerView
        paletteAdapter = RecyclerViewAdapter_Palette(palettes) { clickedPalette ->
            selectedPaletteId = clickedPalette.id
            ToastGenerator.showShortToast("${clickedPalette.colorCode} 가 선택되었습니다.", this)
        }
        recyclerView.adapter = paletteAdapter
        bottomSheetBinding.paletteRecyclerView.layoutManager = GridLayoutManager(this, 5)
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
                var paletteId = folder.palette.id
                if (selectedPaletteId != null) {
                    paletteId = selectedPaletteId
                }
                updateFolder(folder.id, folder.name, paletteId)
                dialog.dismiss()
            }.start()
        }
    }

    /**
     * 투두리스트 폴더 수정 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    private fun updateFolder(
        folderId: Long?,
        newName: String,
        paletteId: Long?,
    ) {
        todoFolderRepository.update(
            accessToken, folderId, newName, paletteId, this,
            onResponse = { response ->
                adapter.notifyDataSetChanged()
            },
            onFailure = {
                ToastGenerator.showShortToast("폴더 수정에 실패하였습니다", this)
            }
        )
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
                deleteFolder(folder.id)
            }.start()
        }
    }

    /**
     * 투두리스트 폴더 삭제 API 호출
     * @since 2024.01.25
     * @author 김유빈
     */
    private fun deleteFolder(
        folderId: Long?,
    ) {
        todoFolderRepository.delete(accessToken, folderId, this,
            onResponse = { response ->
                adapter.notifyDataSetChanged()
            },
            onFailure = {
                ToastGenerator.showShortToast("폴더 삭제에 실패하였습니다", this)
            }
        )
        dialog.dismiss()
    }
}
