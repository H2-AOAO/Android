package kr.sesac.aoao.android.todofolder.model.request

data class FolderSaveRequest(
    val content: String,
    val date: String,
    val paletteId: Long
)
