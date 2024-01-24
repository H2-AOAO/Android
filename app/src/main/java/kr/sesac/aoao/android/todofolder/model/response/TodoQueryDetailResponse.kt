package kr.sesac.aoao.android.todofolder.model.response

data class TodoQueryDetailResponse(
    val check: Int,
    val folders: List<TodoFolderDetailResponse>
)
