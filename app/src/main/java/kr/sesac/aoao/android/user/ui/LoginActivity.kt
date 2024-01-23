package kr.sesac.aoao.android.user.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.databinding.ActivityMainBinding
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.user.model.LoginRequest
import kr.sesac.aoao.android.user.model.Token
import kr.sesac.aoao.android.user.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var input_id: String = ""
    var input_pw: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login_button = findViewById<Button>(R.id.login_button)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)


        login_button.setOnClickListener{
            input_id = email.text.toString()
            input_pw = password.text.toString()

            val loginRequest = LoginRequest()
            loginRequest.email = input_id
            loginRequest.password = input_pw

            loginStart(loginRequest)
        }
    }

    private fun loginStart(loginRequest: LoginRequest) {
        val service = RetrofitConnection.getInstance().create(UserService::class.java)

        service.login(loginRequest).enqueue(object : Callback<ApplicationResponse<Token>> {

            override fun onResponse(
                call: Call<ApplicationResponse<Token>>,
                response: Response<ApplicationResponse<Token>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        // 응답이 성공하면 응답 데이터와 Token의 message를 출력
                        Log.e("응답", "Response Code: ${response.code()}")
                        Log.e("응답", "Data: $data")
                        Log.e("응답", "Token Message: ${data.message}")
                    } else {
                        // 응답이 실패하면 오류 메시지를 출력
                        Log.e("응답", "로그인 실패 - ${data?.message}")
                        Log.e("응답", "Data: $data")
                    }
                } else {
                    // 요청이 실패하면 응답 코드를 출력

                    val errorBody = response.errorBody()?.string()
                    Log.e("응답-3", "Error Response Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<ApplicationResponse<Token>>, t: Throwable) {
                // 요청이 실패한 경우
                Log.e("통신 실패: ", t.localizedMessage)
            }
        })
    }


}
