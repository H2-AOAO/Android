package kr.sesac.aoao.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.sesac.aoao.android.databinding.ActivityMainBinding
import kr.sesac.aoao.android.user.ui.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

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
