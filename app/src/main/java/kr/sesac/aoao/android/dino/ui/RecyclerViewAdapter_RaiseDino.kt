package kr.sesac.aoao.android.dino.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.common.model.ItemResponse
import kr.sesac.aoao.android.dino.model.UserItemResponse
import kr.sesac.aoao.android.dino.service.DinoInfoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @since 2024.01.18
 * @author 김은서
 */
class RecyclerViewAdapter_RaiseDino(private val itemList: List<ItemResponse>,
                                    private val itemClickListener: RecyclerViewItemClickListener, //콜백 함수
                                    private val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter_RaiseDino.RaiseDinoItemViewHolder>() {
    /**
     * RaiseDinoItemViewHolder / getItemCount / onCreateViewHolder / onBindViewHolder / RecyclerViewItemClickListener
     * @since 2024.01.19
     * @author 김은서
     */
    class RaiseDinoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgItem: ImageView = itemView.findViewById(R.id.img_item) //리사이클러뷰의 이미지뷰
        val numTextView: TextView = itemView.findViewById(R.id.num_item) //리사이클러뷰의 텍스트뷰
    }
    override fun getItemCount(): Int { return 4 } //아이템은 총 4개, 한화면에 넣기
    interface RecyclerViewItemClickListener {//콜백 인터페이스
        fun onItemClicked(itemData: ItemResponse)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaiseDinoItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dino_item_button, parent, false)
        return RaiseDinoItemViewHolder(itemView)
    }

    val service = RetrofitConnection.getInstance().create(DinoInfoService::class.java) //구현체 생성
    override fun onBindViewHolder(holder: RaiseDinoItemViewHolder, position: Int) {
        val itemData = itemList[position] //현재 아이템
        val btnItem: Button = holder.itemView.findViewById(R.id.btn_item) //해당 뷰의 사용버튼과 묶기

        /**
         * 유저가 소유한 해당 아이템의 개수 가져오기
         * @since 2024.01.22
         * @author 김은서
         */
        // 유저가 해당 아이템을 몇개 가지고 있는지 통신을 통해서 받아오기,  단순 조회는 status 공백으로 처리
        service.getItemInfo(1, itemData.id.toInt(), " ").enqueue(object : Callback<ApplicationResponse<UserItemResponse>> {
            override fun onResponse(
                call: Call<ApplicationResponse<UserItemResponse>>,
                response: Response<ApplicationResponse<UserItemResponse>>
            ) {
                val applicationResponse = response.body()
                if (applicationResponse?.success == true) { //통신이 성공적이라면
                    val numOfItem = applicationResponse.date //리스폰스의 바디에 들어있는 현재 유저의 해당 아이템 개수를
                    holder.numTextView.text = numOfItem?.itemNum.toString() //텍스트뷰의 텍스트에 저장
                    if (holder.numTextView.text.toString() == "0") btDisable(btnItem) //만약 0개라면 해당 아이템은 사용 불가 -> 버튼 막고 회색으로 변경
                }
            }
            override fun onFailure(call: Call<ApplicationResponse<UserItemResponse>>, t: Throwable) {
                holder.numTextView.text = "-1" //통신에 실패했다면 -1로 초기화
            }
        })

        /**
         * 아이템 사용하기
         * @since 2024.01.22
         * @author 김은서
         */
        btnItem.setOnClickListener { // 아이템 개수와 경험치 조절
            if(holder.numTextView.text.toString() != "0"){ // 사용 가능하다면
                service.getItemInfo(1, itemData.id.toInt(), "사용").enqueue(object : Callback<ApplicationResponse<UserItemResponse>> {
                    override fun onResponse(
                        call: Call<ApplicationResponse<UserItemResponse>>,
                        response: Response<ApplicationResponse<UserItemResponse>>
                    ) {
                        val applicationResponse = response.body()
                        if (applicationResponse?.success == true) { //통신이 성공했다면
                            val numOfItem = applicationResponse.date
                            holder.numTextView.text = numOfItem?.itemNum.toString() //업데이트된 개수로 ui의 개수도 변경
                            itemClickListener.onItemClicked(itemData) //경험치 조절을 위한 콜백 호출
                            if (holder.numTextView.text.toString() == "0") btDisable(btnItem) //사용해서 개수가 0개가 되었다면 버튼 비활성화
                        }
                    }
                    override fun onFailure(call: Call<ApplicationResponse<UserItemResponse>>, t: Throwable) {
                        holder.numTextView.text = "-1"
                    }
                })
            }
            else btDisable(btnItem)
        }

        val drawableResId = when (itemData.name) { //각 이름에 맞는 이미지 파일로 매핑
            "item_food" -> R.drawable.item_food
            "item_medicine" -> R.drawable.item_medicine
            "item_play" -> R.drawable.item_play
            else -> R.drawable.item_love
        }
        val backgroundColor_green = ContextCompat.getColor(context, R.color.raise_dino_dark_green) //텍스트 백그라운드 색깔 설정
        val backgroundDrawable = holder.numTextView.background as GradientDrawable
        backgroundDrawable.setColor(backgroundColor_green)
        holder.imgItem.setImageDrawable(ContextCompat.getDrawable(context, drawableResId)) //이미지 로드
    }

    /**
     * 버튼 비활성화
     * @since 2024.01.23
     * @author 김은서
     */
    private fun btDisable(btnItem: Button){
        var colorResId = R.color.light_grey //회색으로 색을 변경하여 사용자에게 가시적으로 사용이 불가함을 알림
        val color = ContextCompat.getColor(context, colorResId)
        btnItem.backgroundTintList = ColorStateList.valueOf(color)
        btnItem.isEnabled = false //버튼 비활성화
    }
}

