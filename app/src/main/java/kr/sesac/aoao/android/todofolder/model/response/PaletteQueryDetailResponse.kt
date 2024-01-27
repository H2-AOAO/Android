package kr.sesac.aoao.android.todofolder.model.response

import kr.sesac.aoao.android.model.PaletteData

/**
 * @since 2024.01.28
 * @author 김유빈
 */
data class PaletteQueryDetailResponse(
    val palettes: MutableList<PaletteDetailResponse>
) {

    /**
     * Palette Response DTO 를 Data 클래스 형식으로 변경
     * @since 2024.01.28
     * @author 김유빈
     */
    fun convertToData() =
        palettes
            .map { palette ->
                PaletteData(
                    palette.paletteId,
                    palette.colorCode,
                )
            }
            .toMutableList()
}
