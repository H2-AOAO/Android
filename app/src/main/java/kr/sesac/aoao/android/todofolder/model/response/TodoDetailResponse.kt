package kr.sesac.aoao.android.todofolder.model.response

data class TodoDetailResponse(
    val todoId: Long,
    val content: String,
    val checked: Boolean
)
