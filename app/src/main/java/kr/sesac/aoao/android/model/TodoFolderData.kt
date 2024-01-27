package kr.sesac.aoao.android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoFolderData(
    val id: Long?,
    var name: String,
    var palette: PaletteData,
    var todos: MutableList<TodoData>
) : Parcelable {

    companion object {

        fun save() : TodoFolderData {
            return TodoFolderData(
                null,
                "New Folder",
                PaletteData.save(),
                mutableListOf()
            )
        }
    }
}
