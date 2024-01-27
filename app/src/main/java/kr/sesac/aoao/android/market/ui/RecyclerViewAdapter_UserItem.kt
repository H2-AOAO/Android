package kr.sesac.aoao.android.market.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.common.model.ItemResponse
import kr.sesac.aoao.android.market.service.MarketUtil
import java.util.ArrayList

/**
 * @since 2024.01.23
 * @author 김은서
 */
class RecyclerViewAdapter_UserItem(private val itemList: List<ItemResponse>,
                                   private val userItemList: IntArray?,
                                   private val access: String,
                                   private val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter_UserItem.UserItemViewHolder>() {
    /**
     * UserItemViewHolder / getItemCount / onCreateViewHolder / onBindViewHolder
     * @since 2024.01.23
     * @author 김은서
     */
    class UserItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgItem: ImageView = itemView.findViewById(R.id.user_item_market) //리사이클러뷰의 이미지뷰
        val itemNum: TextView = itemView.findViewById(R.id.user_item_market_text) //리사이클러뷰의 텍스트뷰
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_dino_market_user_item, parent, false)
        return RecyclerViewAdapter_UserItem.UserItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 4
    } //아이템은 총 4개, 한화면에 넣기

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        val itemData = itemList[position]
        val drawableResId = MarketUtil.getMappingItemIMG(itemData.name)
        holder.imgItem.setImageDrawable(ContextCompat.getDrawable(context, drawableResId))

        holder.itemNum.text = userItemList?.get(itemData.id.toInt() - 1).toString()
    }
}