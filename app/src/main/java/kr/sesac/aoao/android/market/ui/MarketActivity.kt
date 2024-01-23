package kr.sesac.aoao.android.market.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.GlobalVarApp
import kr.sesac.aoao.android.databinding.ActivityMarketBinding
import kr.sesac.aoao.android.databinding.ActivityRaiseDinoBinding
import kr.sesac.aoao.android.common.model.ItemResponse
import kr.sesac.aoao.android.dino.ui.RaiseDinoActivity

/**
 * 아이템 마켓 액티비티
 * @since 2024.01.18
 * @author 김은서
 */
class MarketActivity : AppCompatActivity() {
    lateinit var binding: ActivityMarketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.recyclerView

        val globalApp = application as GlobalVarApp
        val itemList = globalApp.itemList //전역변수 아이템 리스트

        val adapter = RecyclerViewAdapter_Market(itemList, this)
        recyclerView.adapter = adapter

        val userPoint = intent.getIntExtra("point", -1)
        binding.numUserMoneyMarket.text = userPoint.toString()
        binding.btnGotoHome.setOnClickListener {
            val intent = Intent(this, RaiseDinoActivity::class.java)
            startActivity(intent)
        }
    }
}