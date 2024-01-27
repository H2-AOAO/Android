package kr.sesac.aoao.android.dino.ui

import android.content.Context
import android.content.res.ColorStateList
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
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.model.ItemResponse
import kr.sesac.aoao.android.dino.model.request.ItemNumRequset
import kr.sesac.aoao.android.dino.service.DinoInfoService
import kr.sesac.aoao.android.dino.service.DinoInfoUtil
import kr.sesac.aoao.android.dino.service.DinoInfoUtil.btDisable
import kr.sesac.aoao.android.market.service.MarketUtil


/**
 * @since 2024.01.18
 * @author 김은서
 */
class RecyclerViewAdapter_RaiseDino(private val itemList: List<ItemResponse>,
                                    private val access : String,
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
        fun getUserItemNum(idx:Int, num : Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaiseDinoItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dino_item_button, parent, false)
        return RaiseDinoItemViewHolder(itemView)
    }

    val service = RetrofitConnection.getInstance().create(DinoInfoService::class.java) //구현체 생성
    override fun onBindViewHolder(holder: RaiseDinoItemViewHolder, position: Int) {
        val itemData = itemList[position] //현재 아이템
        val btnItem: Button = holder.itemView.findViewById(R.id.btn_item) //해당 뷰의 사용버튼과 묶기

        fun coroutineSetUserItem(status:String){
            CoroutineScope(Dispatchers.Main).launch {
                val number = userItemAsync(itemData.id.toInt(), status).await()
                holder.numTextView.text = number.toString()
                itemClickListener.getUserItemNum(itemData.id.toInt(), number)
                if (number == 0) btDisable(btnItem, context) //만약 0개라면 해당 아이템은 사용 불가 -> 버튼 막고 회색으로 변경
            }
        }
        coroutineSetUserItem("조회")
         btnItem.setOnClickListener { // 아이템 개수와 경험치 조절
            if(holder.numTextView.text.toString() != "0") {
                coroutineSetUserItem("사용") // 아이템 개수
                itemClickListener.onItemClicked(itemData)
            }
            else btDisable(btnItem, context)
        }

        val drawableResId = MarketUtil.getMappingItemIMG(itemData.name)
        val backgroundColor_green = ContextCompat.getColor(context, R.color.raise_dino_dark_green) //텍스트 백그라운드 색깔 설정
        val backgroundDrawable = holder.numTextView.background as GradientDrawable
        backgroundDrawable.setColor(backgroundColor_green)
        holder.imgItem.setImageDrawable(ContextCompat.getDrawable(context, drawableResId)) //이미지 로드
    }


    /**
     * 아이템 조절하기 - 조회 및 사용
     * @since 2024.01.22
     * @author 김은서
     */
    fun CoroutineScope.userItemAsync(itemId: Int, status: String): Deferred<Int> = async(Dispatchers.Main) {
        //비동기 처리
        val itemNumRequset = ItemNumRequset(itemId, status)
        val deferred = CompletableDeferred<Int>()
        DinoInfoUtil.saveUserItem(access,
            itemNumRequset,
            RaiseDinoActivity(),
            onResponse = { response ->
                if (response.success) deferred.complete(response.date!!.itemNum)
                else deferred.complete(0)
            },
            onFailure = {deferred.complete(0)
            }
        )
        deferred.await() // 비동기적으로 결과를 기다림
    }
}
