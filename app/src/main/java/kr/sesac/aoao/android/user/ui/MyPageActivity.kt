package kr.sesac.aoao.android.user.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.databinding.ActivityMyPageBinding
import kr.sesac.aoao.android.user.service.UserRepository
import kotlin.properties.Delegates

/**
 * @since 2024.01.23
 * @author 이상민
 */
class MyPageActivity : AppCompatActivity(), View.OnClickListener {

    private val userRepository = UserRepository

    private lateinit var accessToken: String
    private var userId by Delegates.notNull<Long>()

    private lateinit var binding: ActivityMyPageBinding

    private lateinit var userImg : ImageView
    private lateinit var nickname : TextView
    private lateinit var email : TextView
    private lateinit var profileEditButton : Button
    private lateinit var pwEditButton : Button

    // 투두
    private lateinit var monthEditTextView : TextView
    private lateinit var monthText : TextView
    private lateinit var finishEditTextView : TextView
    private lateinit var todayEditTextView : TextView

    private fun initializeViews() {
        userImg = binding.userImg
        nickname = binding.nickname
        email = binding.email
        profileEditButton = binding.profileEditButton
        pwEditButton = binding.pwEditButton
        monthEditTextView = binding.monthEditTextView // 이번달 총 투두
        monthText = binding.monthText
        finishEditTextView = binding.finishEditTextView
        todayEditTextView = binding.todayEditTextView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeViews()

        accessToken = TokenManager.getAccessTokenWithTokenType(this)
        userId = TokenManager.getUserId(this).toLong()
        getMypage()
    }

    /**
     * 마이 페이지 API 호출
     * @since 2024.01.27
     * @author 이상민
     */
    private fun getMypage() {
        userRepository.getMypage(accessToken, userId, this@MyPageActivity,
            onResponse = { response ->
                if (response.success && response.date != null) {
                    val result = response.date!!
                    nickname.text = result.nickname
                    email.text = result.email
                    monthText.text = result.month
                    monthEditTextView.text = result.monthSumTodo.toString()
                    finishEditTextView.text = result.sumTodo.toString()
                    todayEditTextView.text = result.today
                }
            },
            onFailure = {
            })
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}
