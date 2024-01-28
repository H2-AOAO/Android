package kr.sesac.aoao.android.todofolder.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kr.sesac.aoao.android.R

class RecyclerViewAdapter_Checked (
    private val checkedId: Long
) : RecyclerView.Adapter<RecyclerViewAdapter_Checked.CheckedViewHolder>() {

    class CheckedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkedItem: ImageView = view.findViewById(R.id.checked_palette)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter_Checked.CheckedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_checked_palette_item, parent, false)
        return CheckedViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckedViewHolder, position: Int) {
        val idx = position + 9
        if(idx == checkedId.toInt()) holder.checkedItem.setVisibility(View.VISIBLE);
        else holder.checkedItem.setVisibility(View.INVISIBLE)
    }

    override fun getItemCount(): Int { return 10 }

}
