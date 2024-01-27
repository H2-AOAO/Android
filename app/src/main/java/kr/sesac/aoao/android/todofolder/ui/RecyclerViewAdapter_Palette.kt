package kr.sesac.aoao.android.todofolder.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kr.sesac.aoao.android.R
import kr.sesac.aoao.android.model.PaletteData

/**
 * @since 2024.01.28
 * @author 김유빈
 */
class RecyclerViewAdapter_Palette(
    private val palettes: List<PaletteData>,
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

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: PaletteViewHolder, position: Int) {
        val palette = palettes[position]

        // 팔레트 색상 지정
        val newTint: Int = Color.parseColor(palette.colorCode)
        holder.palette.backgroundTintList = ColorStateList.valueOf(newTint)
    }
}
