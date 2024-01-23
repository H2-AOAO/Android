package kr.sesac.aoao.android.dino.ui

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class RecyclerViewAdapter_RaiseDino(private val itemList: List<ItemResponse>,
                                    private val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter_RaiseDino.RaiseDinoItemViewHolder>() {
    override fun getItemCount(): Int { return 4 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaiseDinoItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dino_item_button, parent, false)
        return RaiseDinoItemViewHolder(itemView)
    }

    val service = RetrofitConnection.getInstance().create(DinoInfoService::class.java)
    override fun onBindViewHolder(holder: RaiseDinoItemViewHolder, position: Int) {
        val itemData = itemList[position]
        service.getItemInfo(1, itemData.id.toInt(), " ").enqueue(object : Callback<ApplicationResponse<UserItemResponse>> {
            override fun onResponse(
                call: Call<ApplicationResponse<UserItemResponse>>,
                response: Response<ApplicationResponse<UserItemResponse>>
            ) {
                val applicationResponse = response.body()
                if (applicationResponse?.success == true) {
                    val numOfItem = applicationResponse.date
                    holder.numTextView.text = numOfItem?.itemNum.toString()
                }
            }
            override fun onFailure(call: Call<ApplicationResponse<UserItemResponse>>, t: Throwable) {
                holder.numTextView.text = "-1"
            }
        })

        val drawableResId = when (itemData.name) {
            "item_food" -> R.drawable.item_food
            "item_medicine" -> R.drawable.item_medicine
            "item_play" -> R.drawable.item_play
            else -> R.drawable.item_love
        }
        val backgroundColor_green2 = ContextCompat.getColor(context, R.color.raise_dino_dark_green)
        val backgroundDrawable = holder.numTextView.background as GradientDrawable
        backgroundDrawable.setColor(backgroundColor_green2)
        holder.imgItem.setImageDrawable(ContextCompat.getDrawable(context, drawableResId))
    }

    class RaiseDinoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgItem: ImageView = itemView.findViewById(R.id.img_item)
        val numTextView: TextView = itemView.findViewById(R.id.num_item)
    }
}
