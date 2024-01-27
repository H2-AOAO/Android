package kr.sesac.aoao.android.market.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.GlobalVarApp
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.common.model.ItemResponse
import kr.sesac.aoao.android.databinding.ActivityMarketBinding
import kr.sesac.aoao.android.dino.model.request.ItemNumRequset
import kr.sesac.aoao.android.dino.model.response.UserItemResponse
import kr.sesac.aoao.android.dino.service.DinoInfoService
import kr.sesac.aoao.android.dino.service.DinoInfoUtil
import kr.sesac.aoao.android.dino.ui.RaiseDinoActivity
import kr.sesac.aoao.android.market.model.request.UsePointRquest
import kr.sesac.aoao.android.market.service.MarketUtil
import kr.sesac.aoao.android.model.ItemData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.raise_dino_light_green) //상태바 색 전체와 맞추기
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색으로
        }
        val globalApp = application as GlobalVarApp
        val itemList = globalApp.itemList //전역변수 아이템 리스트

        val accessToken = TokenManager.getAccessTokenWithTokenType(this)

        var userItemNumForPutExtra = intent.getIntArrayExtra("userItemNumForPutExtra")
        Log.d("asdasd", userItemNumForPutExtra.toString())
        val recyclerViewItem = binding.recyclerViewItemRecycle //유저가 소유한 아이템 개수 보여줄 리사이클러뷰
        val adapter_item = RecyclerViewAdapter_UserItem(itemList,userItemNumForPutExtra, accessToken!!, this)
        recyclerViewItem.adapter = adapter_item

        var userPoint = intent.getIntExtra("point", -1) //이전 액티비티로부터 넘겨받은 값 불러오기
        binding.numUserMoneyMarket.text = userPoint.toString() //UI에 적용
        val recyclerView = binding.recyclerView
        adapter = RecyclerViewAdapter_Market(itemList,userPoint,
            object : RecyclerViewAdapter_Market.RecyclerViewItemClickListener{
                override fun onItemClicked(itemData: ItemResponse) { //아이템을 구매한 경우 호출되는 콜백함수

                    CoroutineScope(Dispatchers.Main).launch {
                        plusItem(accessToken, itemData.id.toInt(), "구매").await()

                    }
                    userPoint -= itemData.price //유저 포인트에 변경된 값 할당
                    binding.numUserMoneyMarket.text = userPoint.toString()//UI에 적용
                    adapter.notifyDataSetChanged() //유저의 포인트가 변경되었으므로 해당 어댑터 통보, -> 구매할 수 없는 아이템의 구매버튼 비활성화를 위해

                    val currentNum = userItemNumForPutExtra!!.get(itemData.id.toInt() - 1)
                    if(itemData.id.toInt() == 5){
                        for(i in 0..3) userItemNumForPutExtra[i] += 1
                    }
                    else userItemNumForPutExtra[itemData.id.toInt() - 1] += 1
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

    fun CoroutineScope.plusItem(accessToken: String, itemId: Int, status: String): Deferred<Int> = async(Dispatchers.Main) {
        //비동기 처리
        val itemNumRequset = ItemNumRequset(itemId, status)
        val deferred = CompletableDeferred<Int>()
        DinoInfoUtil.saveUserItem(accessToken,
            itemNumRequset,
            this@MarketActivity,
            onResponse = { response ->
                Log.d("res", response.toString())
            },
            onFailure = {deferred.complete(0)
            }
        )
        deferred.await() // 비동기적으로 결과를 기다림
    }
    fun minusPoint(accessToken: String, itemId : Int, activity: Activity){
        val usePointRquest = UsePointRquest(itemId)
        MarketUtil.usePoint(
            accessToken,
            usePointRquest,
            activity,
            onResponse = {
                response->
                if(!response.success) Log.d("error", "에러 발생- ${response.date}")
            },
            onFailure = {}
        )
    }
}