package kr.sesac.aoao.android.user.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.user.model.response.ProfileResponse
import kr.sesac.aoao.android.user.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @since 2024.01.23
 * @author 이상민
 */
class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        // 저장된 Access Token을 가져옴
        val accessToken = TokenManager.getAccessToken(this)

        Log.e("token", "${accessToken}")
        
        // Access Token이 있는 경우에만 서버에 요청
        if (!accessToken.isNullOrBlank()) {
            // 서버로부터 유저 정보를 가져와 UI에 표시
            getUserInfoFromServer(accessToken)
        } else {
            // Access Token이 없는 경우에는 로그인 화면으로 이동 또는 다른 처리 수행
            // 예: 로그인 화면으로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // 현재 MyPageActivity 종료
        }

    }

    private fun getUserInfoFromServer(accessToken: String) {
        val myPageService = RetrofitConnection.getInstance().create(UserService::class.java)
        val call = myPageService.getProfile("Bearer $accessToken")
        call.enqueue(object : Callback<ApplicationResponse<ProfileResponse>> {
            override fun onResponse(
                call: Call<ApplicationResponse<ProfileResponse>>,
                response: Response<ApplicationResponse<ProfileResponse>>
            ) {
                val applicationResponse = response.body()
                Log.e("결과", "${applicationResponse?.toString()}")

                if (applicationResponse != null) {
                    Log.e("결과", "${applicationResponse.date?.nickname}")
                }

                Log.e("결과", "${applicationResponse?.date?.nickname}")
                if (applicationResponse?.success == true) {
//                    Log.e("결과", applicationResponse.toString())
                }


                if (response.isSuccessful) {
                    // 서버에서 받아온 유저 정보를 UI에 표시
                    val userInfo = response.body()

                    Log.e("결과", userInfo.toString())
//                    welcomeMessageTextView.text = "Welcome, ${userInfo?.username}!"
                    // 다른 UI 요소에도 필요한 정보를 표시할 수 있음
                } else {
                    // 서버 응답 오류 처리
                    // 예: 토큰 만료, 권한 부족 등에 대한 처리
                }
            }

            override fun onFailure(call: Call<ApplicationResponse<ProfileResponse>>, t: Throwable) {
                // 요청이 실패한 경우
                Log.e("통신 실패: ", t.localizedMessage)
            }
        })
    }
}
