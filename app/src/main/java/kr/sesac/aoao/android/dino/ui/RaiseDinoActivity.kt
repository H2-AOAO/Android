package kr.sesac.aoao.android.dino.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kr.sesac.aoao.android.market.ui.MarketActivity
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.GlobalVarApp
import kr.sesac.aoao.android.databinding.ActivityRaiseDinoBinding
import kr.sesac.aoao.android.dino.model.DinoResponse
import kr.sesac.aoao.android.dino.service.DinoInfoService
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.common.model.ErrorResponse
import kr.sesac.aoao.android.dino.model.DinoLvResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 공룡 기르기 액티비티
 * @since 2024.01.18
 * @author 김은서
 */
class RaiseDinoActivity : AppCompatActivity(){
    lateinit var binding : ActivityRaiseDinoBinding
    private var userPoint: Int = 0
    lateinit private var targetDino : DinoLvResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //상태바 글자색 검은색으로
        }
        binding = ActivityRaiseDinoBinding.inflate(layoutInflater) //바인딩
        setContentView(binding.root)


        val recyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager //리사이클러뷰 설정


        val globalApp = application as GlobalVarApp
        val itemList = globalApp.itemList //전역변수 아이템 리스트

        val adapter = RecyclerViewAdapter_RaiseDino(itemList,this)
        recyclerView.adapter = adapter

        getDinoInfo(2);
        binding.btnGotoMarket.setOnClickListener { //마켓으로 이동
            val intent = Intent(this, MarketActivity::class.java)
            intent.putExtra("point", userPoint)
            startActivity(intent)
        }
    }
    private fun setImg(imgName:String){
        val resourceId = resources.getIdentifier(imgName, "drawable", packageName)
        binding.dinoImg.setImageResource(resourceId) //이미지 변경 완료
    }

    private fun setName(dinoName:String){
        binding.dinoName.text = dinoName
    }
    private fun setExp(currExp : Int, dinoLv: DinoLvResponse){
        val percent = (currExp.toDouble()) / dinoLv.exp.toDouble()
        val formattedPercent = String.format("%.1f", percent * 100) + "%"
        binding.dinoExp.text = formattedPercent
        //352dp
        val density = resources.displayMetrics.density
        val userWidthPx = (352 * percent * density).toInt()
        //dp 기준 사용자의 퍼센트에 맞는 경험지 바 너비
        //픽셀로 변환 -> LayoutParams은 픽셀값이어야함
        val layoutParams = binding.dinoExpBarUser.layoutParams
        layoutParams.width = userWidthPx.toInt()
        binding.dinoExpBarUser.layoutParams = layoutParams
    }
    private fun setBarColor(color : String) {
        //다이노 색상에 맞추어 하단바 색상 변경
        val barColorStart = "raise_dino_bar_" + color + "_start"
        val barColorEnd = "raise_dino_bar_" + color + "_end"
        val resourceStart = resources.getIdentifier(barColorStart, "color", packageName)
        val resourceEnd = resources.getIdentifier(barColorEnd, "color", packageName)

        val colorStart = ContextCompat.getColor(this@RaiseDinoActivity, resourceStart)
        val colorEnd = ContextCompat.getColor(this@RaiseDinoActivity, resourceEnd)

        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(colorStart, colorEnd)
        )
        binding.dinoExpBarUserColor.background = gradientDrawable
    }
    private fun setLv(lv: Int, exp:Int) {
        val globalApp = application as GlobalVarApp
        val dinoList = globalApp.dinoList //전역변수 다이노 리스트

        targetDino = dinoList.first() { it.lv == lv }
        binding.dinoLvName.text = "Lv" + lv + " " + targetDino.name //다이노 레벨명 설정
        setExp(exp, targetDino)
    }
    private fun setDinoStatus(dinoResponse : DinoResponse?){
        if (dinoResponse != null) {
            userPoint = dinoResponse.point
            binding.numUserMoney.text = userPoint.toString()

            setName(dinoResponse.name)
            setImg("dino_" + dinoResponse.color + "_lv" + dinoResponse.lv)
            setLv(dinoResponse.lv, dinoResponse.exp)
            setBarColor(dinoResponse.color)
            }
        }
    private fun getNewDino(){

    }
    private fun gotoLogin(){
        //액티비티 로그인 페이지로 변경하기
        val intent = Intent(this, MarketActivity::class.java)
        startActivity(intent);
    }
    private fun gotoTodo(){
        //액티비티 투두 페이지로 변경하기
        val intent = Intent(this, MarketActivity::class.java)
        startActivity(intent);
    }
    private fun showToast(msg : String?){
        Toast.makeText(this@RaiseDinoActivity, msg, Toast.LENGTH_SHORT).show()
    }
    private fun getDinoInfo(userId: Long){
        val service = RetrofitConnection.getInstance() .create(DinoInfoService::class.java) //DinoInfoService 구현체 생성
        //네트워크 요청 실행
        service.getDinoInfo(userId).enqueue(object : Callback<ApplicationResponse<DinoResponse>> {
            override fun onResponse(
                call: Call<ApplicationResponse<DinoResponse>>,
                response: Response<ApplicationResponse<DinoResponse>>
            ) {
                val applicationResponse = response.body()
                if(applicationResponse?.success == true){
                    val dinoResponse = applicationResponse.date
                    setDinoStatus(dinoResponse)
                }
                else if(applicationResponse?.success == null){
                    val errorBodyString = response.errorBody()?.string()
                    try {
                        val errorMsg = Gson().fromJson(errorBodyString, ErrorResponse::class.java).message
                        showToast(errorMsg)
                        Log.e("응답", "Error Message: $errorMsg")

                        if(errorMsg.equals("사용자를 찾을 수 없습니다.")) gotoLogin()// 유저 정보를 못찾으면 로그인 페이지로 이동
                        else getNewDino() //공룡 정보 없으면 새로운 공룡 만들기
                    } catch (e: Exception) {
                        showToast("네트워크 문제가 생겼습니다. 다시 시도해주세요.")
                        Log.e("응답", "Error parsing error body: ${e.message}")
                    }
                }
            }

            override fun onFailure(call: Call<ApplicationResponse<DinoResponse>>, t: Throwable) {
                showToast("서버 문제가 생겼습니다. 다시 시도해주세요.")
                Log.e("응답-e", "Message: ${t.message}")
                gotoTodo()
            }
        })
    }
}








//    fun setOnClickGoToDo(view: View){
//        var intent = Intent(this, MarketActivity::class.java)
//        startActivity(intent)
//    }
//    fun setOnClickGoFriend(view: View){
//        var intent = Intent(this, MarketActivity::class.java)
//        startActivity(intent)
//    }
//    fun setOnClickGoMypage(view: View){
//        var intent = Intent(this, MarketActivity::class.java)
//        startActivity(intent)
//    }