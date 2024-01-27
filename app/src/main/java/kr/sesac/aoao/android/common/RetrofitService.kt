package kr.sesac.aoao.android.common

import android.app.Activity
import android.util.Log
import com.google.gson.Gson
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.common.model.ErrorResponse
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
                Log.d("res", response.toString())
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
                onFailure(t)
            }
        })
    }
}
