package kr.sesac.aoao.android.common

import android.app.Activity
import android.util.Log
import android.widget.Toast
import kr.sesac.aoao.android.common.model.ApplicationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RetrofitService {

    fun <T> connect(
        call: Call<ApplicationResponse<T>>,
        onSuccess: (T) -> Unit,
        onError: (String?) -> Unit,
        context: Activity,
    ) {
        call.enqueue(object : Callback<ApplicationResponse<T>> {
            override fun onResponse(
                call: Call<ApplicationResponse<T>>,
                response: Response<ApplicationResponse<T>>
            ) {
                val applicationResponse = response.body()
                if (applicationResponse?.success == true) {
                    val data = applicationResponse.date
                    onSuccess(data!!)
                } else if (applicationResponse?.success == null) {
                    val errorBodyString = response.errorBody()?.string()
                    onError(errorBodyString)
                }
            }

            override fun onFailure(call: Call<ApplicationResponse<T>>, t: Throwable) {
                showErrorMessage(context)
                Log.e("응답-e", "Message: ${t.message}")
            }
        })
    }

    private fun showErrorMessage(context: Activity) {
        Toast.makeText(context, "서버 문제가 생겼습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
    }
}
