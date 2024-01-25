package kr.sesac.aoao.android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoData(
    var id: Long?,
    var content: String,
    var checked: Boolean
) : Parcelable {

    companion object {

        fun save() : TodoData {
            return TodoData(null,"New Todo", false)
        }
    }
}
