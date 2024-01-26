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
 * @since 2024.01.18
 * @author 김은서
 */
class MarketActivity : AppCompatActivity() {
    lateinit var binding: ActivityMarketBinding
    private lateinit var adapter: RecyclerViewAdapter_Market //포인트 업데이트 시 구매 불가능한 버튼 비활성화를 위해 어댑터에게 통보할 목적으로 먼저 선언

    /**
     * onCreate
     * @since 2024.01.18
     * @author 김은서
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.raise_dino_light_green) //상태바 색 전체와 맞추기
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색으로

        val globalApp = application as GlobalVarApp
        val itemList = globalApp.itemList //전역변수 아이템 리스트

        val recyclerViewItem = binding.recyclerViewItemRecycle //유저가 소유한 아이템 개수 보여줄 리사이클러뷰
        val adapter_item = RecyclerViewAdapter_UserItem(itemList, this)
        recyclerViewItem.adapter = adapter_item

        var userPoint = intent.getIntExtra("point", -1) //이전 액티비티로부터 넘겨받은 값 불러오기
        binding.numUserMoneyMarket.text = userPoint.toString() //UI에 적용
        val recyclerView = binding.recyclerView
        adapter = RecyclerViewAdapter_Market(itemList,userPoint,
            object : RecyclerViewAdapter_Market.RecyclerViewItemClickListener{
                override fun onItemClicked(newPoint: Int) { //아이템을 구매한 경우 호출되는 콜백함수
                    userPoint = newPoint //유저 포인트에 변경된 값 할당
                    binding.numUserMoneyMarket.text = userPoint.toString()//UI에 적용
                    adapter.notifyDataSetChanged() //유저의 포인트가 변경되었으므로 해당 어댑터 통보, -> 구매할 수 없는 아이템의 구매버튼 비활성화를 위해
                    adapter_item.notifyDataSetChanged() //유저가 소유한 아이템 개수가 변경되었음을 해당 어댑터에게 통보
                }
            },
            this)
        recyclerView.adapter = adapter

        binding.btnGotoHome.setOnClickListener { //공룡 키우기로 돌아가는 버튼
            val intent = Intent(this, RaiseDinoActivity::class.java)
            startActivity(intent)
        }
    }
}