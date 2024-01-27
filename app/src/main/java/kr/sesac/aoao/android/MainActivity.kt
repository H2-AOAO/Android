package kr.sesac.aoao.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kr.sesac.aoao.android.databinding.ActivityMainBinding
import kr.sesac.aoao.android.user.ui.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        window.statusBarColor = ContextCompat.getColor(this, R.color.dino_background_color)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색으로

        val gifResourceId = resources.getIdentifier("moving_dino", "raw", packageName )
        Glide.with(this).load(gifResourceId).into(binding.dino)

        setEmailLoginButtonOnClickListener()

        setContentView(binding.root)
    }

    private fun setEmailLoginButtonOnClickListener() {
        binding.emailLoginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
