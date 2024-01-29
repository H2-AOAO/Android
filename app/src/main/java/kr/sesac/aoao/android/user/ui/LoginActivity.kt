package kr.sesac.aoao.android.user.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.calendar.ui.HomeActivity
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.databinding.ActivityLoginBinding
import kr.sesac.aoao.android.user.model.request.LoginRequest
import kr.sesac.aoao.android.user.service.AuthRepository

/**
 * @since 2024.01.23
 * @author 이상민
 */
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val authRepository = AuthRepository
    private lateinit var binding: ActivityLoginBinding

    private lateinit var email: EditText
    private lateinit var loginEmailErrorMessageTextview: TextView

    private lateinit var password: EditText
    private lateinit var loginPwErrorMessageTextview: TextView

    private lateinit var signButton : Button
    private lateinit var loginButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeViews()

        window.statusBarColor = ContextCompat.getColor(this, R.color.white) //상태바 색깔 하얀색
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색
    }

    /**
     * 뷰 초기화
     *
     * @since 2024.01.28
     * @author 이상민
     */
    private fun initializeViews() {
        email = binding.email
        loginEmailErrorMessageTextview = binding.loginEmailErrorMessageTextview
        password = binding.password
        loginPwErrorMessageTextview = binding.loginPwErrorMessageTextview
        signButton = binding.signButton
        loginButton = binding.loginButton

        signButton.setOnClickListener(this)
        loginButton.setOnClickListener(this)
    }

    /**
     * 클릭 이벤트
     *
     * @since 2024.01.28
     * @author 이상민
     */
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.signButton -> {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
            R.id.loginButton -> {
                if(email.text.toString().trim() == ""){
                    updateUIOnUiThread("아이디를 입력해주세요.", loginEmailErrorMessageTextview, R.color.error)
                }else{
                    loginEmailErrorMessageTextview.text = ""
                }

                if(password.text.toString().trim() == ""){
                    updateUIOnUiThread("비밀번호를 입력해주세요.", loginPwErrorMessageTextview, R.color.error)
                }else{
                    loginPwErrorMessageTextview.text = ""
                }

                login(LoginRequest(email.text.toString(), password.text.toString()))
            }
        }
    }

    /**
     * UI 업데이트를 위한 runOnUiThread 사용
     *
     * @since 2024.01.25
     * @author 이상민
     */
    private fun updateUIOnUiThread(message: String, textView: TextView, color: Int) {
        runOnUiThread {
            textView.text = message
            textView.setTextColor(ContextCompat.getColor(this@LoginActivity, color))
        }
    }

    /**
     * 로그인 API 호출
     *
     * @return
     * @author 이상민
     * @since 2024.01.24
     */
    fun login(request: LoginRequest) {
        authRepository.login(request,
            this,
            onResponse = { response ->
                if (response.success && response.date != null) {
                    TokenManager.setAccessToken(this@LoginActivity, "${response.date?.accessToken}")
                    TokenManager.setRefreshToken(this@LoginActivity, "${response.date?.refreshToken}")
                    TokenManager.setUserId(this@LoginActivity, "${response.date?.userId}")
                    successLogin()
                }
            },
            onFailure = { response ->
                handleErrorMessage(response.message)
            })
    }


    /**
     * UI 업데이트 함수
     *
     * @since 2024.01.25
     * @author 이상민
     */
    private fun handleErrorMessage(trimmedErrorMsg: String?) {
        println()
        if (trimmedErrorMsg =="올바른 형식의 이메일 주소여야 합니다."){
            updateUIOnUiThread(trimmedErrorMsg, loginEmailErrorMessageTextview, R.color.error)
        }
        if(trimmedErrorMsg == "가입되지 않은 Email 입니다.") {
            updateUIOnUiThread(trimmedErrorMsg, loginEmailErrorMessageTextview, R.color.error)
        }

        if (trimmedErrorMsg == "비밀번호는 8~20 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.") {
            updateUIOnUiThread(trimmedErrorMsg, loginPwErrorMessageTextview, R.color.error)
        }
        if(trimmedErrorMsg == "잘못된 비밀번호입니다."){
            updateUIOnUiThread(trimmedErrorMsg, loginPwErrorMessageTextview, R.color.error)
        }
    }

    /**
     * 로그인 성공 함수
     *
     * @since 2024.01.25
     * @author 이상민
     */
    private fun successLogin() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
        finish() // 현재 로그인 화면 종료
    }

}
