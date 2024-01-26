package kr.sesac.aoao.android.todofolder.model.response

data class TodoFolderDetailResponse(
    val folderId: Long,
    val colorCode: String,
    val content: String,
    val todos: List<TodoDetailResponse>
)
