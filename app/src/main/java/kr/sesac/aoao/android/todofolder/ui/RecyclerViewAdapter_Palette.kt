package kr.sesac.aoao.android.todofolder.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.model.Palette
import kr.sesac.aoao.android.model.PaletteData

/**
 * @since 2024.01.28
 * @author 김유빈
 */
class RecyclerViewAdapter_Palette(
    private val palettes: List<PaletteData>,
    private val onItemClick: (PaletteData) -> Unit,
) : RecyclerView.Adapter<RecyclerViewAdapter_Palette.PaletteViewHolder>() {

    class PaletteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val palette: ImageView = view.findViewById(R.id.palette)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaletteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_palette_item, parent, false)
        return PaletteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return palettes.size
    }

    override fun onBindViewHolder(holder: PaletteViewHolder, position: Int) {
        val palette = palettes[position]

        // 팔레트 이미지 지정
        holder.palette.setImageResource(Palette.from(palette.colorCode).layoutId)

        // 아이템 클릭 이벤트 처리
        holder.palette.setOnClickListener {
            onItemClick(palette)
        }
    }
}
