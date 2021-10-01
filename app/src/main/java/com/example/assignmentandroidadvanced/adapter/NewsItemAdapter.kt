package com.example.assignmentandroidadvanced.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.model.News
import com.squareup.picasso.Picasso

class NewsItemAdapter(
    private val mContext: Context,
    private val mResource: Int,
    private val newsList: ArrayList<News>
) : ArrayAdapter<News>(mContext, mResource, newsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(mContext).inflate(mResource, null)
        val title = view.findViewById<TextView>(R.id.txtTitle)
        val des = view.findViewById<TextView>(R.id.txtDes)
        val image:ImageView = view.findViewById(R.id.imgNews)

        title.text = newsList[position].title
        des.text = newsList[position].description
        Picasso.get().load(newsList[position].imageLink).resize(150, 150).error(R.drawable.no_image_available).into(image)

        return view
    }
}