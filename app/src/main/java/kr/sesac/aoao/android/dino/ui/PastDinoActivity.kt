package kr.sesac.aoao.android.dino.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.ToastGenerator
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.common.model.ItemResponse
import kr.sesac.aoao.android.databinding.ActivityPastDinoBinding
import kr.sesac.aoao.android.databinding.ActivityRaiseDinoBinding
import kr.sesac.aoao.android.databinding.DialogDinoLv5Binding
import kr.sesac.aoao.android.dino.model.request.ItemNumRequset
import kr.sesac.aoao.android.dino.model.response.PastDinoResponse
import kr.sesac.aoao.android.dino.service.DinoInfoUtil
import kr.sesac.aoao.android.market.ui.RecyclerViewAdapter_Market

/**
 * 과거 다이노 페이지
 * @since 2024.01.28
 * @author 김은서
 */
class PastDinoActivity : AppCompatActivity(){
    lateinit var binding : ActivityPastDinoBinding
    var pastDinoList : List<PastDinoResponse> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPastDinoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white) //상태바 색깔 하얀색
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색

        val access = TokenManager.getAccessTokenWithTokenType(this)
        CoroutineScope(Dispatchers.Main).launch {
            val result = getPastDino(access).await() // getPastDino의 완료를 기다림
            if (result == 1 && pastDinoList.size > 0){
                binding.notHavePastDino.visibility = View.INVISIBLE
                val recyclerView = binding.recyclerView
                val layoutManager = GridLayoutManager(this@PastDinoActivity, 2)
                recyclerView.layoutManager = layoutManager
                val adapter = RecyclerViewAdapter_pastDino(pastDinoList,this@PastDinoActivity)
                recyclerView.adapter = adapter
            }
        }
        binding.goBackToDino.setOnClickListener{
            val intent = Intent(this, RaiseDinoActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    /**
     * 과거 다이노 조회
     * @since 2024.01.28
     * @author 김은서
     */
    private fun CoroutineScope.getPastDino(access: String) : Deferred<Int> = async(Dispatchers.Main) {
        //비동기 처리로 다이노 정보 받을때까지는 결과 보여주지 않음
        val deferred = CompletableDeferred<Int>()
        DinoInfoUtil.pastDino(access, PastDinoActivity(),
            onResponse = {
                response ->
                if(response.success){
                    pastDinoList = response.date ?: emptyList()
                    deferred.complete(1) //요청 성공 및 비동기 작업 완료 알림용
                }
            },
            onFailure = {
                response -> ToastGenerator.showShortToast(response.message,this@PastDinoActivity);
            }
        )
        deferred.await() // 비동기적으로 결과를 기다림
    }
}