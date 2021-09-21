package com.example.assignmentandroidadvanced.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.databinding.MainActivityItemCardLayoutBinding
import com.example.assignmentandroidadvanced.model.CardItem

class CardViewAdapter(private val mContext: Context, private val list: ArrayList<CardItem>) :
    RecyclerView.Adapter<CardViewAdapter.ViewHolder>() {

    private val binding: MainActivityItemCardLayoutBinding =
        MainActivityItemCardLayoutBinding.inflate(
            LayoutInflater.from(mContext)
        )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById<ImageView>(R.id.imgItemMainActivity)
        val itemName: TextView = itemView.findViewById<TextView>(R.id.txtItemNameMainActivity)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.main_activity_item_card_layout, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.setImageResource(list[position].resID)
        holder.itemName.text = list[position].name
    }

    override fun getItemCount(): Int {
        return list.size
    }

}