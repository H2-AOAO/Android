package kr.sesac.aoao.android.dino.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kr.sesac.aoao.android.market.ui.MarketActivity
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.BottomNavigationHandler
import kr.sesac.aoao.android.common.GlobalVarApp
import kr.sesac.aoao.android.databinding.ActivityRaiseDinoBinding
import kr.sesac.aoao.android.dino.model.response.DinoResponse
import kr.sesac.aoao.android.dino.service.DinoInfoService
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.ToastGenerator
import kr.sesac.aoao.android.common.TokenManager
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.common.model.ErrorResponse
import kr.sesac.aoao.android.common.model.ItemResponse
import kr.sesac.aoao.android.dino.model.request.ExpRequset
import kr.sesac.aoao.android.dino.model.response.DinoLvResponse
import kr.sesac.aoao.android.dino.service.DinoInfoUtil
import kr.sesac.aoao.android.dino.service.NewDinoUtil
import kr.sesac.aoao.android.user.ui.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @since 2024.01.18
 * @author 김은서
 */
class RaiseDinoActivity : AppCompatActivity(){
    lateinit var binding : ActivityRaiseDinoBinding
    private var userPoint: Int = 0 //마켓으로 넘길 유저의 포인트
    var userItemNumForPutExtra :MutableList<Int> = mutableListOf(0,0,0,0,0)
    lateinit private var targetDino : DinoLvResponse //액티비티 전역에서 접근할 현재 다이노 정보
    private var exp : Int = 0 //다이노 현재 경험치
    private var totalExp : Int = 0 //현재 레벨의 총 경험치 -> 레벨업 구분용
    private var dinoLv : Int = 0 //현재 다이노의 레벨
    private var dinoColor : String = ""

    /**
     * onCreate
     * @since 2024.01.18
     * @author 김은서
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white) //상태바 색깔 하얀색
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색

        binding = ActivityRaiseDinoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        BottomNavigationHandler(this,"DinoActivity").attach(bottomNavigationView)

        val access = TokenManager.getAccessTokenWithTokenType(this)

        val recyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) //가로로 정렬
        recyclerView.layoutManager = layoutManager // 리사이클러뷰 설정

        val globalApp = application as GlobalVarApp
        val itemList = globalApp.itemList // 전역변수 아이템 리스트
        val adapter = RecyclerViewAdapter_RaiseDino(itemList, access!!, object :
            RecyclerViewAdapter_RaiseDino.RecyclerViewItemClickListener {
            override fun onItemClicked(itemData: ItemResponse) { //아이템이 클릭되었을때 실행될 콜백 함수
                var newExp = exp + itemData.exp //현재 경험치 + 선택된 아이템의 경험치
                if(newExp >= totalExp) { //레벨업을 하는가 판단
                    dinoLv += 1
                    newExp -= totalExp
                }
                itemClickedUpExp(access, dinoLv, newExp) //변경된 경험치에 맞추어 ui 세팅
            }
            override fun getUserItemNum(idx:Int, num : Int){
                userItemNumForPutExtra[idx - 1] = num
            }
        }, this)
        recyclerView.adapter = adapter

        getDinoInfo(access); //유저의 다이노 정보 가져오기
        binding.btnGotoMarket.setOnClickListener { //마켓으로 이동
            val intent = Intent(this, MarketActivity::class.java)
            intent.putExtra("point", userPoint) //마켓으로 현재 유저의 포인트 넘기기
            intent.putExtra("userItemNumForPutExtra", userItemNumForPutExtra.toIntArray())
            startActivity(intent)
        }
    }
    private fun gotoNewDino(){
        val intent = Intent(this, NewDinoActivity::class.java)
        startActivity(intent)
        finish()
    }
    /**
     * 다이노 정보로 UI 로드 - setImg / setName / setExp / setBarColor / setLv / setDinoStatus
     * @since 2024.01.23
     * @author 김은서
     */
    private fun setImg(imgName:String){ //유저의 다이노 정보에 맞는 이미지 렌더링
        val resourceId = resources.getIdentifier(imgName, "drawable", packageName)
        binding.dinoImg.setImageResource(resourceId) //이미지 변경 완료
    }
    private fun setName(dinoName:String){ //유저의 다이노 이름
        binding.dinoName.text = dinoName
    }
    private fun setExp(currExp : Int, total :Int, currLv : Int){ //현재 경험치, 현재 레벨의 한계 경험치, 현재 레벨
        exp = currExp
        totalExp = total
        dinoLv = currLv
        // 변수 할당
        if(dinoLv == 5){//다이노 성장 완료, 새로운 다이노 키우기
            dinoLv = 1
            val intent = Intent(this, DialogActivity::class.java)
            intent.putExtra("color", dinoColor)
            startActivity(intent)
        }
        val percent = (exp.toDouble()) / totalExp.toDouble() //경험치바에 보여줄 퍼센트
        val formattedPercent = String.format("%.1f", percent * 100) + "%" //소수점 한자리수까지 보여줌
        binding.dinoExp.text = formattedPercent

        val density = resources.displayMetrics.density
        val userWidthPx = (350 * percent * density).toInt() //경험치바 총 길이가 352dp
        //사용자의 퍼센트에 맞는 경험지 바 너비
        val layoutParams = binding.dinoExpBarUser.layoutParams
        layoutParams.width = userWidthPx  //픽셀로 변환 -> LayoutParams은 픽셀값이어야함
        binding.dinoExpBarUser.layoutParams = layoutParams
    }
    private fun setBarColor(color : String) {//다이노 색상에 맞추어 하단바 색상 변경
        val barColorStart = "raise_dino_bar_" + color + "_start" //그라데이션 색상 시작값
        val barColorEnd = "raise_dino_bar_" + color + "_end" //그라데이션 색상 끝값
        val resourceStart = resources.getIdentifier(barColorStart, "color", packageName)
        val resourceEnd = resources.getIdentifier(barColorEnd, "color", packageName)

        val colorStart = ContextCompat.getColor(this@RaiseDinoActivity, resourceStart)
        val colorEnd = ContextCompat.getColor(this@RaiseDinoActivity, resourceEnd)

        val gradientDrawable = GradientDrawable( //그라데이션 시작색과 끝 색 할당
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(colorStart, colorEnd)
        )
        binding.dinoExpBarUserColor.background = gradientDrawable
    }
    private fun setLv(lv: Int, exp:Int) { //현재 다이노 레벨에 맞는 레벨명 보여주기
        val globalApp = application as GlobalVarApp
        val dinoList = globalApp.dinoList //전역변수 다이노 리스트
        targetDino = dinoList.first() { it.lv == lv } //다이노 레벨에 맞는 다이노 정보
        binding.dinoLvName.text = "Lv" + lv + " " + targetDino.name //다이노 레벨명 설정
        setExp(exp, targetDino.exp, targetDino.lv) //다이노 레벨에 맞는 경험치바 설정
    }
    private fun setDinoStatus(dinoResponse : DinoResponse){ // 다이노 정보를 받아왔다면 ui를 정보에 맞추어 변경
        userPoint = dinoResponse.point
        binding.numUserMoney.text = userPoint.toString() //포인트
        dinoColor = dinoResponse.color
        setName(dinoResponse.name) //이름
        if (dinoResponse.lv == 1) setImg("dino_egg") // 1단계는 색상에 관계없이 모두 같은 이름.
        else setImg("dino_" + dinoResponse.color + "_lv" + dinoResponse.lv) //1단계가 아니라면 각 레벨과 색상에 맞는 이미지 로드
        setLv(dinoResponse.lv, dinoResponse.exp) //레벨명 - 경험치바
        setBarColor(dinoResponse.color) // 경험치바 색상
    }

    /**
     * 다이노 정보 조회
     * @since 2024.01.23
     * @author 김은서
     */
    private fun getDinoInfo(access: String){ //서버로부터 다이노 정보 가져오기
        DinoInfoUtil.getDinoInfo(access, this,
            onResponse = {
                response ->
                if(response.success){
                    val dinoResponse = response.date //데이터값 받은 후, UI 변경
                    setDinoStatus(dinoResponse!!)
                }
            },
            onFailure = {
                ToastGenerator.showShortToast("공룡을 생성해주세요!", this@RaiseDinoActivity)
                gotoNewDino()
            }
        )
    }

    /**
     * 다이노 경험치 업데이트
     * @since 2024.01.23
     * @author 김은서
     */
    private fun itemClickedUpExp(access: String, currLv: Int, currExp : Int){ //아이템을 사용하여 경험치가 올랐을 때
        val newExpRequset = ExpRequset(currLv, currExp)
        DinoInfoUtil.UpExp(
            access,
            newExpRequset,
            this,
            onResponse = {
                response ->
                if(response.success) setDinoStatus(response.date!!)
            },
            onFailure = {}
        )
    }
}

