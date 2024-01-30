package kr.sesac.aoao.android.market.ui

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.model.ItemResponse
import kr.sesac.aoao.android.dino.service.DinoInfoUtil.btDisable
import kr.sesac.aoao.android.market.service.MarketUtil.getMappingItemIMG
import kr.sesac.aoao.android.market.service.MarketUtil.getMappingItemName

/**
 * @since 2024.01.18
 * @author 김은서
 */
class RecyclerViewAdapter_Market(private val itemList: List<ItemResponse>,
                                 private var userPrice : Int,
                                 private val itemClickListener: RecyclerViewAdapter_Market.RecyclerViewItemClickListener,
                                 private val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter_Market.MarketItemViewHolder>()
{
    /**
     * MarketItemViewHolder / RecyclerViewItemClickListener / onCreateViewHolder / onBindViewHolder / getItemCount
     * @since 2024.01.19
     * @author 김은서
     */
    class MarketItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imgItem: ImageView = itemView.findViewById(R.id.market_item_img) //리사이클러뷰의 이미지뷰
        val itemName :TextView = itemView.findViewById(R.id.market_item_name) //리사이클러뷰의 텍스트뷰 - 아이템 이름
        val price : TextView = itemView.findViewById(R.id.market_item_money) //리사이클러뷰의 텍스트뷰 - 아이템 가격
    }
    interface RecyclerViewItemClickListener {//콜백 인터페이스
        fun onItemClicked(itemData: ItemResponse)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dino_market_item, parent, false)
        return RecyclerViewAdapter_Market.MarketItemViewHolder(itemView)
    }

    override fun getItemCount(): Int { return 5 } //아이템은 총 4개, 한화면에 넣기

    override fun onBindViewHolder(holder: MarketItemViewHolder, position: Int) {
        val itemData = itemList[position]

        val drawableResId = getMappingItemIMG(itemData.name)
        val item_name = getMappingItemName(itemData.name)

        val buttonBuy: Button = holder.itemView.findViewById(R.id.market_button_buy)//해당 뷰의 구용버튼과 묶기
        val price = itemData.price
        if(price > userPrice) btDisable(buttonBuy, context) // 구매가 불가능하면 비활성화

        buttonBuy.setOnClickListener {
            if(userPrice >= price) {
                userPrice -= price
                itemClickListener.onItemClicked(itemData) //콜백 호출
            }
        }

        val backgroundColor_green = ContextCompat.getColor(context, R.color.raise_dino_green) //색상 변경
        val backgroundDrawable = holder.imgItem.background as GradientDrawable
        backgroundDrawable.setColor(backgroundColor_green)
        //UI에 데이터 적용
        holder.imgItem.setImageDrawable(ContextCompat.getDrawable(context, drawableResId))
        holder.itemName.text = item_name
        holder.price.text = itemData.price.toString()


    }
}
