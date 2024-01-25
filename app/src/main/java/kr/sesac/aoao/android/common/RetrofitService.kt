package kr.sesac.aoao.android.common

import android.app.Activity
import kr.sesac.aoao.android.common.model.ApplicationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RetrofitService {

    fun <T> connect(
        call: Call<ApplicationResponse<T>>,
        context: Activity,
        onResponse: (ApplicationResponse<T>) -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        call.enqueue(object : Callback<ApplicationResponse<T>> {
            override fun onResponse(
                call: Call<ApplicationResponse<T>>,
                response: Response<ApplicationResponse<T>>
            ) {
                if (response.isSuccessful) {
                    onResponse(response.body()!!)
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    ToastGenerator.showShortToast(errorBodyString, context)
                    onFailure(Throwable("Unsuccessful response: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ApplicationResponse<T>>, t: Throwable) {
                t.printStackTrace()
                ToastGenerator.showShortToast("서버 문제가 생겼습니다. 다시 시도해주세요.", context)
                onFailure(t)
            }
        })
    }
}
