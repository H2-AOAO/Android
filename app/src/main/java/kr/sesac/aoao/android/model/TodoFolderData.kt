package kr.sesac.aoao.android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoFolderData(
    val id: Long?,
    var name: String,
    var colorCode: String,
    var todos: MutableList<TodoData>
) : Parcelable {

    companion object {

        fun save() : TodoFolderData {
            return TodoFolderData(
                null,
                "New Folder",
                "blue",
                mutableListOf()
            )
        }
    }
}
