package com.example.assignmentandroidadvanced.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.activity.CourseActivity
import com.example.assignmentandroidadvanced.activity.MapActivity
import com.example.assignmentandroidadvanced.activity.NewsActivity
import com.example.assignmentandroidadvanced.activity.SocialActivity
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
        val cardView:CardView = itemView.findViewById(R.id.cardViewItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.main_activity_item_card_layout, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.setImageResource(list[position].resID)
        holder.itemName.text = list[position].name

        holder.cardView.setOnClickListener {
            when(position) {
                0 -> mContext.startActivity(Intent(mContext, CourseActivity::class.java))
                1 -> mContext.startActivity(Intent(mContext, MapActivity::class.java))
                2 -> mContext.startActivity(Intent(mContext, NewsActivity::class.java))
                3 -> mContext.startActivity(Intent(mContext, SocialActivity::class.java))
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}