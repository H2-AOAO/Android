package kr.sesac.aoao.android.market.ui

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.model.ItemResponse
/**
 * 마켓 리사이클러뷰 액티비티
 * @since 2024.01.18
 * @author 김은서
 */
class RecyclerViewAdapter_Market(private val itemList: List<ItemResponse>, private val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter_Market.MarketItemViewHolder>()
{
    class MarketItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imgItem: ImageView = itemView.findViewById(R.id.market_item_img)
        val itemName :TextView = itemView.findViewById(R.id.market_item_name)
        val price : TextView = itemView.findViewById(R.id.market_item_money)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dino_market_item, parent, false)
        return RecyclerViewAdapter_Market.MarketItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 5
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
        val backgroundColor_green = ContextCompat.getColor(context, R.color.raise_dino_green)
        val backgroundDrawable = holder.imgItem.background as GradientDrawable
        backgroundDrawable.setColor(backgroundColor_green)
        holder.imgItem.setImageDrawable(ContextCompat.getDrawable(context, drawableResId))
        holder.itemName.text = item_name
        holder.price.text = itemData.price.toString()
    }
}