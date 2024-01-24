package kr.sesac.aoao.android.market.ui

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
 * @since 2024.01.23
 * @author 김은서
 */
class RecyclerViewAdapter_UserItem(private val itemList: List<ItemResponse>,
                                   private val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter_UserItem.UserItemViewHolder>() {
    /**
     * UserItemViewHolder / getItemCount / onCreateViewHolder / onBindViewHolder
     * @since 2024.01.23
     * @author 김은서
     */
    class UserItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgItem: ImageView = itemView.findViewById(R.id.user_item_market) //리사이클러뷰의 이미지뷰
        val itemNum : TextView = itemView.findViewById(R.id.user_item_market_text) //리사이클러뷰의 텍스트뷰
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dino_market_user_item,parent,false)
        return RecyclerViewAdapter_UserItem.UserItemViewHolder(itemView)
    }

    override fun getItemCount(): Int { return 4 } //아이템은 총 4개, 한화면에 넣기

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        val itemData = itemList[position]
        val drawableResId = when (itemData.name) { //각 이름에 맞는 이미지로 매핑
            "item_food" -> R.drawable.item_food
            "item_medicine" -> R.drawable.item_medicine
            "item_play" -> R.drawable.item_play
            else -> R.drawable.item_love
        }
        holder.imgItem.setImageDrawable(ContextCompat.getDrawable(context,drawableResId))

        /**
         * 각 아이템을 유저가 얼마나 소유했는지 가져오기
         * @since 2024.01.23
         * @author 김은서
         */
        val service = RetrofitConnection.getInstance().create(DinoInfoService::class.java)
        service.getItemInfo(1, itemData.id.toInt(), " ").enqueue(object :
            Callback<ApplicationResponse<UserItemResponse>> {
            override fun onResponse(
                call: Call<ApplicationResponse<UserItemResponse>>,
                response: Response<ApplicationResponse<UserItemResponse>>
            ) {
                val applicationResponse = response.body()
                if (applicationResponse?.success == true) { //통신 성공
                    val numOfItem = applicationResponse.date
                    holder.itemNum.text = numOfItem?.itemNum.toString() //적용
                }
            }
            override fun onFailure(call: Call<ApplicationResponse<UserItemResponse>>, t: Throwable) {
                holder.itemNum.text = "-1"
            }
        })

    }
}
