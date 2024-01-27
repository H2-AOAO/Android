package kr.sesac.aoao.android.todofolder.model.response

import kr.sesac.aoao.android.model.PaletteData
import kr.sesac.aoao.android.model.TodoFolderData

/**
 * @since 2024.01.25
 * @author 김유빈
 */
data class FolderQueryDetailResponse(
    val folders: MutableList<FolderDetailResponse>
) {

    /**
     * Folder Response DTO 를 Data 클래스 형식으로 변경
     * @since 2024.01.25
     * @author 김유빈
     */
    fun convertToData() =
        folders
            .map { folder ->
                TodoFolderData(
                    folder.folderId,
                    folder.content,
                    PaletteData.find(folder.colorCode),
                    mutableListOf()
                )
            }
            .toMutableList()
}
