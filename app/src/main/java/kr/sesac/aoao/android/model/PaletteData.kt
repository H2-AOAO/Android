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
                "#FA602F"
            )
        }

        fun find(colorCode: String) : PaletteData {
            return PaletteData(
                null,
                colorCode
            )
        }
    }
}
