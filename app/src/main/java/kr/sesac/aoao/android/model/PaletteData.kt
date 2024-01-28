package kr.sesac.aoao.android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaletteData(
    val id: Long?,
    var colorCode: String,
) : Parcelable {

    companion object {

        fun save() : PaletteData {
            return PaletteData(
                null,
                "pink"
            )
        }

        fun find(id: Long, colorCode: String) : PaletteData {
            return PaletteData(
                id,
                colorCode
            )
        }
    }
}
