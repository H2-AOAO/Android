package kr.sesac.aoao.android.todofolder.model.request

data class FolderUpdateRequest(
    val content: String,
    val paletteId: Long?
)
