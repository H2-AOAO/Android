package kr.sesac.aoao.android.user.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.databinding.ActivityMyPageBinding
import kr.sesac.aoao.android.databinding.ActivityPasswordEditBinding
import kr.sesac.aoao.android.user.service.UserRepository

/**
 * @since 2024.01.28
 * @author 이상민
 */
class PasswordEditActivity : AppCompatActivity(), View.OnClickListener {

    private val userRepository = UserRepository
    private lateinit var binding: ActivityPasswordEditBinding

    private lateinit var backButton: ImageView

    // 현재 비밀번호
    private lateinit var currentPw: EditText
    private lateinit var currentPwErrorMessageTextview: TextView
    private lateinit var currentPwCheckButton: Button

    // 새 비밀번호
    private lateinit var newPw: EditText
    private lateinit var pwErrorMessageTextview: TextView

    // 확인 비밀번호
    private lateinit var newPw2: EditText
    private lateinit var pw2ErrorMessageTextview: TextView

    private lateinit var changeButton: Button

    private fun initializeViews() {
        currentPw = binding.currentPw
        currentPwErrorMessageTextview = binding.currentPwErrorMessageTextview
        currentPwCheckButton = binding.currentPwCheckButton

        newPw = binding.newPw
        pwErrorMessageTextview = binding.pwErrorMessageTextview

        newPw2 = binding.newPw2
        pw2ErrorMessageTextview = binding.pw2ErrorMessageTextview

        changeButton = binding.changeButton

        // 클릭 이벤트
        currentPwCheckButton.setOnClickListener(this)
        changeButton.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeViews()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.backButton -> {
                onBackPressed()
            }
            R.id.currentPwCheckButton -> {

            }
            R.id.changeButton -> {

            }
        }
    }
}
