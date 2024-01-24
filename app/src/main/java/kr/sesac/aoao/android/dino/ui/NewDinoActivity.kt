package kr.sesac.aoao.android.dino.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.databinding.ActivityNewDinoBinding
import kr.sesac.aoao.android.databinding.ActivityRaiseDinoBinding
import kr.sesac.aoao.android.market.ui.MarketActivity

/**
 * @since 2024.01.24
 * @author 김은서
 */
class NewDinoActivity : AppCompatActivity(){
    lateinit var binding: ActivityNewDinoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.white) //상태바 색깔 하얀색
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색
        }
        binding = ActivityNewDinoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSetDino.setOnClickListener {
            //다이노 업데이트 통신 서비스

            val intent = Intent(this, RaiseDinoActivity::class.java)
            startActivity(intent)
        }

    }
}