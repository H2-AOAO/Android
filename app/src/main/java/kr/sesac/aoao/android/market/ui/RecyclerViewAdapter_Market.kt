package kr.sesac.aoao.android.market.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.RetrofitConnection
import kr.sesac.aoao.android.common.model.ApplicationResponse
import kr.sesac.aoao.android.common.model.ErrorResponse
import kr.sesac.aoao.android.common.model.ItemResponse
import kr.sesac.aoao.android.dino.model.UserItemResponse
import kr.sesac.aoao.android.dino.service.DinoInfoService
import kr.sesac.aoao.android.dino.ui.RecyclerViewAdapter_RaiseDino
import kr.sesac.aoao.android.market.model.MarketResponse
import kr.sesac.aoao.android.market.service.MarketService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 마켓 리사이클러뷰 액티비티
 * @since 2024.01.18
 * @author 김은서
 */
class RecyclerViewAdapter_Market(private val itemList: List<ItemResponse>,
                                 private var userPrice : Int,
                                 private val itemClickListener: RecyclerViewAdapter_Market.RecyclerViewItemClickListener,
                                 private val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter_Market.MarketItemViewHolder>()
{
    class MarketItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imgItem: ImageView = itemView.findViewById(R.id.market_item_img)
        val itemName :TextView = itemView.findViewById(R.id.market_item_name)
        val price : TextView = itemView.findViewById(R.id.market_item_money)
    }
    interface RecyclerViewItemClickListener {
        fun onItemClicked(newPoint: Int)
    } //콜백용 인터페이스
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dino_market_item, parent, false)
        return RecyclerViewAdapter_Market.MarketItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 5
    }

    private fun showToast(msg : String?){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
    override fun onBindViewHolder(holder: MarketItemViewHolder, position: Int) {
        val itemData = itemList[position]

        val drawableResId = when (itemData.name) {
            "item_food" -> R.drawable.item_food
            "item_medicine" -> R.drawable.item_medicine
            "item_play" -> R.drawable.item_play
            "item_love" -> R.drawable.item_love
            else -> R.drawable.item_set
        }
        val item_name = when(itemData.name){
            "item_food" -> "사료"
            "item_medicine" -> "약품"
            "item_play" -> "놀이"
            "item_love" -> "애정"
            else -> "1 세트"
        }

        val btnItem: Button = holder.itemView.findViewById(R.id.market_button)
        val price = itemData.price
        val serviceMarket = RetrofitConnection.getInstance().create(MarketService::class.java)
        val serviceDino = RetrofitConnection.getInstance().create(DinoInfoService::class.java)
        if(price > userPrice) btDisable(btnItem)
        Log.d("응답-가격", "$price, $userPrice")
        btnItem.setOnClickListener {
            serviceMarket.usePoint(1, itemData.id).enqueue(
                object :  Callback<ApplicationResponse<MarketResponse>> {
                    override fun onResponse(call: Call<ApplicationResponse<MarketResponse>>, response: Response<ApplicationResponse<MarketResponse>>) {
                        val res = response.body()
                        if (res?.success == true){
                            val data = res.date
                            userPrice -= price
                            itemClickListener.onItemClicked(data!!.point)
                        }
                        else if(res?.success == null){
                            val errorCode = response.errorBody()?.string()
                            try {
                                val errorMsg = Gson().fromJson(errorCode, ErrorResponse::class.java).message
                                showToast(errorMsg)
                                Log.e("응답", "Error Message: $errorMsg")
                            } catch (e: Exception) {
                                showToast("네트워크 문제가 생겼습니다. 다시 시도해주세요.")
                            }
                        }
                    }
                    override fun onFailure(call: Call<ApplicationResponse<MarketResponse>>, t: Throwable) {
                        showToast("서버에 오류가 발생했습니다. 다시 시도해주세요")
                    }

                }
            )

            if(userPrice >= price) {
                serviceDino.getItemInfo(1, itemData.id.toInt(), "구매")
                    .enqueue(object : Callback<ApplicationResponse<UserItemResponse>> {
                        override fun onResponse(
                            call: Call<ApplicationResponse<UserItemResponse>>,
                            response: Response<ApplicationResponse<UserItemResponse>>
                        ) {
                            val res = response.body()
                            if (res?.success != true) showToast("네트워크 문제가 생겼습니다. 다시 시도해주세요.")
                        }
                        override fun onFailure(call: Call<ApplicationResponse<UserItemResponse>>, t: Throwable) {
                            showToast("서버 문제가 생겼습니다. 다시 시도해주세요.")
                        }
                    })
            }
        }

        val backgroundColor_green = ContextCompat.getColor(context, R.color.raise_dino_green)
        val backgroundDrawable = holder.imgItem.background as GradientDrawable
        backgroundDrawable.setColor(backgroundColor_green)
        holder.imgItem.setImageDrawable(ContextCompat.getDrawable(context, drawableResId))
        holder.itemName.text = item_name
        holder.price.text = itemData.price.toString()
    }

    private fun btDisable(btnItem: Button){
        var colorResId = R.color.light_grey
        val color = ContextCompat.getColor(context, colorResId)
        btnItem.backgroundTintList = ColorStateList.valueOf(color)
        btnItem.isEnabled = false
    }
}