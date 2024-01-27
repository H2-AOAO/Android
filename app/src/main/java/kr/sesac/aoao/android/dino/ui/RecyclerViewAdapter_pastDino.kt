package kr.sesac.aoao.android.dino.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.dino.model.response.PastDinoResponse

class RecyclerViewAdapter_pastDino (private val dinoList: List<PastDinoResponse>,
                                    private val context: Context
) :
    RecyclerView.Adapter<RecyclerViewAdapter_pastDino.PastDinoViewHolder >() {
    /**
     * PastDinoViewHolder / onCreateViewHolder / getItemCount / onBindViewHolder
     * @since 2024.01.28
     * @author 김은서
     */

    class PastDinoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dinoImg: ImageView = itemView.findViewById(R.id.past_dino_img) //리사이클러뷰의 이미지뷰
        val dinoName: TextView = itemView.findViewById(R.id.past_dino_name) //리사이클러뷰의 텍스트뷰
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastDinoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_past_dino, parent, false)
        return PastDinoViewHolder(itemView)
    }

    override fun getItemCount(): Int { return dinoList.size }

    override fun onBindViewHolder(holder: PastDinoViewHolder, position: Int) {
        val pastDino = dinoList[position]
        val dinoColorResId = context.resources.getIdentifier(
            "dino_${pastDino.color}_lv4",
            "drawable",
            context.packageName
        )
        holder.dinoImg.setImageDrawable(ContextCompat.getDrawable(context, dinoColorResId))
        holder.dinoName.setText(pastDino.name)
    }
}