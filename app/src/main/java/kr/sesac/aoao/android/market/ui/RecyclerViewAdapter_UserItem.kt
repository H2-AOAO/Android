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

class RecyclerViewAdapter_UserItem(private val itemList: List<ItemResponse>,
                                   private val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter_UserItem.UserItemViewHolder>() {
    class UserItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgItem: ImageView = itemView.findViewById(R.id.user_item_market)
        val itemNum : TextView = itemView.findViewById(R.id.user_item_market_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dino_market_user_item,parent,false)
        return RecyclerViewAdapter_UserItem.UserItemViewHolder(itemView)
    }

    override fun getItemCount(): Int { return 4 }

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        val itemData = itemList[position]
        val drawableResId = when (itemData.name) {
            "item_food" -> R.drawable.item_food
            "item_medicine" -> R.drawable.item_medicine
            "item_play" -> R.drawable.item_play
            else -> R.drawable.item_love
        }
        holder.imgItem.setImageDrawable(ContextCompat.getDrawable(context,drawableResId))

        val service = RetrofitConnection.getInstance().create(DinoInfoService::class.java)
        service.getItemInfo(1, itemData.id.toInt(), " ").enqueue(object :
            Callback<ApplicationResponse<UserItemResponse>> {
            override fun onResponse(
                call: Call<ApplicationResponse<UserItemResponse>>,
                response: Response<ApplicationResponse<UserItemResponse>>
            ) {
                val applicationResponse = response.body()
                if (applicationResponse?.success == true) {
                    val numOfItem = applicationResponse.date
                    holder.itemNum.text = numOfItem?.itemNum.toString()
                }
            }
            override fun onFailure(call: Call<ApplicationResponse<UserItemResponse>>, t: Throwable) {
                holder.itemNum.text = "-1"
            }
        })

    }
}
