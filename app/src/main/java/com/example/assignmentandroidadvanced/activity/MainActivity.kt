package com.example.assignmentandroidadvanced.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.adapter.CardViewAdapter
import com.example.assignmentandroidadvanced.databinding.ActivityMainBinding
import com.example.assignmentandroidadvanced.model.CardItem

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var itemList:ArrayList<CardItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupMainActivity()
    }

    private fun setupMainActivity() {
        itemList = ArrayList<CardItem>()
        itemList.add(CardItem(baseContext.getString(R.string.course), R.drawable.course_icon))
        itemList.add(CardItem(baseContext.getString(R.string.map), R.drawable.map_icon))
        itemList.add(CardItem(baseContext.getString(R.string.news), R.drawable.news_icon))
        itemList.add(CardItem(baseContext.getString(R.string.social), R.drawable.social_icons))


        binding.rcvMainActivity.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = CardViewAdapter(this@MainActivity, list = itemList )
        }

    }
}