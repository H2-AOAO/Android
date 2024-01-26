package kr.sesac.aoao.android.todofolder.service

import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.todofolder.model.response.PaletteQueryDetailResponse
import retrofit2.Call
import retrofit2.http.GET

/**
 * @since 2024.01.24
 * @author 김유빈
 */
interface PaletteService {

    /**
     * 팔레트 리스트 목록 조회
     * @since 2024.01.24
     * @parameter
     * @return ApplicationResponse<PaletteQueryDetailResponse>
     * @author 김유빈
     */
    @GET("/palettes")
    fun findAll()
    : Call<ApplicationResponse<PaletteQueryDetailResponse>>
}
