package kr.sesac.aoao.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import kr.sesac.aoao.android.calendar.ui.HomeActivity
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.databinding.ActivityMainBinding
import kr.sesac.aoao.android.user.model.response.AccessToken
import kr.sesac.aoao.android.user.model.response.TokenResponse
import kr.sesac.aoao.android.user.service.AuthService
import kr.sesac.aoao.android.user.ui.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색으로

        setEmailLoginButtonOnClickListener()
        setContentView(binding.root)

        // kakao 로그인
        kakaoLogin()
    }

    private fun setEmailLoginButtonOnClickListener() {
        binding.emailLoginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * 카카오 로그인
     * @since 2024.01.26
     * @author 이상민
     */
    private fun kakaoLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {
                Log.d("token 성공 : ", token.accessToken)
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                kakaoLoginApi(token.accessToken)
            }
        }
        val kakaoLoginBtn = findViewById<Button>(R.id.kakao_login_button)
        kakaoLoginBtn.setOnClickListener {
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            }else{
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    private fun kakaoLoginApi(accessToken: String) {
        val service = RetrofitConnection.getInstance().create(AuthService::class.java)

        val accessTokenObject = AccessToken(accessToken)
        val call = service.loginKakao(accessTokenObject)
        call.enqueue(object : Callback<ApplicationResponse<TokenResponse>> {
            override fun onResponse(
                call: Call<ApplicationResponse<TokenResponse>>,
                response: Response<ApplicationResponse<TokenResponse>>
            ) {
                val applicationResponse = response.body()
                Log.e("결과", "${applicationResponse?.toString()}")

                if (applicationResponse != null) {
                    TokenManager.setAccessToken(this@MainActivity, "${applicationResponse.date?.accessToken}")
                    TokenManager.setRefreshToken(this@MainActivity, "${applicationResponse.date?.refreshToken}")

                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                }
            }

            override fun onFailure(call: Call<ApplicationResponse<TokenResponse>>, t: Throwable) {
                // 요청이 실패한 경우
                Log.e("통신 실패: ", t.localizedMessage)
            }
        })
    }
}