package kr.sesac.aoao.android.todofolder.model.response

import kr.sesac.aoao.android.model.TodoData
import kr.sesac.aoao.android.model.TodoFolderData

/**
 * @since 2024.01.25
 * @author 김유빈
 */
data class TodoQueryDetailResponse(
    val check: Int,
    val folders: MutableList<TodoFolderDetailResponse>
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
                    folder.colorCode,
                    convertToTodoData(folder)
                )
            }
            .toMutableList()

    /**
     * TodoResponse DTO 를 Data 클래스 형식으로 변경
     * @since 2024.01.25
     * @author 김유빈
     */
    private fun convertToTodoData(folder: TodoFolderDetailResponse) =
        folder.todos
            .map { todo ->
                TodoData(
                    todo.content,
                    todo.checked
                )
            }
            .toMutableList()
}
