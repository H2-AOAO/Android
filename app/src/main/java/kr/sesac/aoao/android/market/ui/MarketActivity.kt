package kr.sesac.aoao.android.market.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.GlobalVarApp
import kr.sesac.aoao.android.databinding.ActivityMarketBinding
import kr.sesac.aoao.android.databinding.ActivityRaiseDinoBinding
import kr.sesac.aoao.android.common.model.ItemResponse
import kr.sesac.aoao.android.dino.ui.RaiseDinoActivity
import kr.sesac.aoao.android.dino.ui.RecyclerViewAdapter_RaiseDino

/**
 * 아이템 마켓 액티비티
 * @since 2024.01.18
 * @author 김은서
 */
class MarketActivity : AppCompatActivity() {
    lateinit var binding: ActivityMarketBinding
    private lateinit var adapter: RecyclerViewAdapter_Market
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.raise_dino_light_green)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색으로
        }
        val globalApp = application as GlobalVarApp
        val itemList = globalApp.itemList //전역변수 아이템 리스트


        val recyclerViewItem = binding.recyclerViewItemRecycle
        val adapter_item = RecyclerViewAdapter_UserItem(itemList, this)
        recyclerViewItem.adapter = adapter_item

        var userPoint = intent.getIntExtra("point", -1)
        binding.numUserMoneyMarket.text = userPoint.toString()
        val recyclerView = binding.recyclerView
        adapter = RecyclerViewAdapter_Market(itemList,userPoint,
            object : RecyclerViewAdapter_Market.RecyclerViewItemClickListener{
                override fun onItemClicked(newPoint: Int) {
                    userPoint = newPoint
                    binding.numUserMoneyMarket.text = userPoint.toString()
                    setBuyButton()
                    adapter_item.notifyDataSetChanged()
                }
            },
            this)
        recyclerView.adapter = adapter



        binding.btnGotoHome.setOnClickListener {
            val intent = Intent(this, RaiseDinoActivity::class.java)
            startActivity(intent)
        }
    }
    fun setBuyButton(){
        adapter.notifyDataSetChanged()
    }
}