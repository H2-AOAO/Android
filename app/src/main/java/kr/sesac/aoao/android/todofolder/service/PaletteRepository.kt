package kr.sesac.aoao.android.todofolder.service

import android.app.Activity
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.RetrofitService
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.common.model.ErrorResponse
import kr.sesac.aoao.android.todofolder.model.response.PaletteQueryDetailResponse

/**
 * @since 2024.01.28
 * @author 김유빈
 */
object PaletteRepository {

    private val paletteService = RetrofitConnection.getInstance().create(PaletteService::class.java)

    /**
     * 팔레트 리스트 조회 API 호출
     * @since 2024.01.28
     * @author 김유빈
     */
    fun findAll(
        context: Activity,
        onResponse: (ApplicationResponse<PaletteQueryDetailResponse>) -> Unit,
        onFailure: (ErrorResponse) -> Unit,
    ) {
        RetrofitService.connect(
            paletteService.findAll(),
            context, onResponse, onFailure
        )
    }
}
