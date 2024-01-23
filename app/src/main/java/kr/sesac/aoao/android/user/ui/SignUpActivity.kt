package kr.sesac.aoao.android.user.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import kr.sesac.aoao.android.R

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var signup_back: Button

    private lateinit var email_check_button: Button
    private lateinit var email_hint: TextView

    private lateinit var nickname_check_button: Button
    private lateinit var nickname_hint: TextView

    private lateinit var pw_hint: TextView
    private lateinit var pw2_hint: TextView
    private lateinit var signup_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)

        // 뷰 가져오기
//        signup_back = findViewById(R.id.signup_back)
//        email_check_button = findViewById(R.id.email_check_button)
//        email_hint = findViewById(R.id.email_hint)
//        nickname_check_button = findViewById(R.id.nickname_check_button)
//        nickname_hint = findViewById(R.id.nickname_hint)
//        pw_hint = findViewById(R.id.pw_hint)
//        pw2_hint = findViewById(R.id.pw2_hint)
//        signup_button = findViewById(R.id.signup_button)

        // 버튼별 OnClickListener 등록
//        signup_back.setOnClickListener(this)
//        email_check_button.setOnClickListener(this)
//        nickname_check_button.setOnClickListener(this)
//        signup_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
//        when(v?.id){
//            R.id.signup_back -> {back()}
//            R.id.email_check_button -> {emailCheck()}
//            R.id.nickname_check_button -> {nicknameCheck()}
//            R.id.signup_button -> {signup()}
//        }
    }

    private fun back() {
        TODO("Not yet implemented")
    }

    private fun emailCheck() {
        TODO("Not yet implemented")
    }

    private fun nicknameCheck() {
        TODO("Not yet implemented")
    }

    private fun signup() {
        TODO("Not yet implemented")
    }

}