package kr.sesac.aoao.android.todofolder.model.response

data class FolderDetailResponse(
    val folderId: Long,
    val paletteId: Long,
    val colorCode: String,
    val content: String
)
