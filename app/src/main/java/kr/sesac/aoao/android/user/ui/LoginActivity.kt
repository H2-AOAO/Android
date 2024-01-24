package kr.sesac.aoao.android.user.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.calendar.ui.HomeActivity
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.common.model.ErrorResponse
import kr.sesac.aoao.android.user.model.LoginRequest
import kr.sesac.aoao.android.user.model.TokenResponse
import kr.sesac.aoao.android.user.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var emailErrorMessageTextView: TextView
    private lateinit var pwErrorMessageTextView: TextView

    private var input_id: String = ""
    private var input_pw: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailErrorMessageTextView = findViewById(R.id.email_error_message_textview)
        pwErrorMessageTextView = findViewById(R.id.pw_error_message_textview)

        val login_button = findViewById<Button>(R.id.login_button)
        val sign_button = findViewById<Button>(R.id.sign_button)

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)

        sign_button.setOnClickListener{
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        login_button.setOnClickListener {
            input_id = email.text.toString()
            input_pw = password.text.toString()

            val loginRequest = LoginRequest()
            loginRequest.email = input_id
            loginRequest.password = input_pw

//            if (input_id.trim() == "") emailErrorMessageTextView.text = "아이디를 입력하세요."
//            else emailErrorMessageTextView.text = ""
//
//            if (input_pw.trim() == "") pwErrorMessageTextView.text = "비밀번호를 입력하세요."
//            else pwErrorMessageTextView.text = ""

            loginStart(loginRequest)
        }
    }

    private fun loginStart(loginRequest: LoginRequest) {
        val service = RetrofitConnection.getInstance().create(UserService::class.java)
        service.login(loginRequest).enqueue(object : Callback<ApplicationResponse<TokenResponse>> {
            override fun onResponse(
                call: Call<ApplicationResponse<TokenResponse>>,
                response: Response<ApplicationResponse<TokenResponse>>
            ) {
                val applicationResponse = response.body()
                if (applicationResponse?.success == true) {
//                    val token = applicationResponse.date
//                    Log.e("응답", "accessToken: ${applicationResponse.date?.accessToken}")
//                    Log.e("응답", "refreshToken: ${applicationResponse.date?.refreshToken}")

                    TokenManager.setAccessToken(this@LoginActivity, "${applicationResponse.date?.accessToken}")
                    TokenManager.setRefreshToken(this@LoginActivity, "${applicationResponse.date?.refreshToken}")
                    successLogin()
                } else if (applicationResponse?.success == null) {
                    val errorBodyString = response.errorBody()?.string()
                    try {
                        val errorMsg = Gson().fromJson(errorBodyString, ErrorResponse::class.java).message
                        val trimmedErrorMsg = errorMsg?.trim()

                        if (trimmedErrorMsg == "올바른 형식의 이메일 주소여야 합니다") {
                            emailErrorMessageTextView.text = errorMsg
                        } else if (trimmedErrorMsg == "가입되지 않은 email 입니다") {
                            emailErrorMessageTextView.text = errorMsg
                        }

                        if (trimmedErrorMsg == "비밀번호는 8~20 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다") {
                            pwErrorMessageTextView.text = errorMsg
                        } else if (trimmedErrorMsg == "잘못된 비밀번호입니다") {
                            pwErrorMessageTextView.text = errorMsg
                        }

                    } catch (e: Exception) {
                        Log.e("응답", "Error parsing error body: ${e.message}")
                    }
                }
            }

            override fun onFailure(call: Call<ApplicationResponse<TokenResponse>>, t: Throwable) {
                // 요청이 실패한 경우
                Log.e("통신 실패: ", t.localizedMessage)
            }
        })
    }
    private fun successLogin() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
        finish() // 현재 로그인 화면 종료
    }

}
