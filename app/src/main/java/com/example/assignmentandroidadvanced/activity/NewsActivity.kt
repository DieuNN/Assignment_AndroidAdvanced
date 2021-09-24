package com.example.assignmentandroidadvanced.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.XMLParse
import com.example.assignmentandroidadvanced.adapter.NewsItemAdapter
import com.example.assignmentandroidadvanced.databinding.ActivityNewsBinding
import com.example.assignmentandroidadvanced.model.News
import org.w3c.dom.Element
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.URL

private var newsList = ArrayList<News>()
private var adapter: NewsItemAdapter? = null
class NewsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNewsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val content = RSSFeed().execute("https://vnexpress.net/rss/giao-duc.rss")

        adapter = NewsItemAdapter(this, R.layout.news_items, newsList)

        binding.listView.adapter = adapter

        binding.listView.setOnItemClickListener { _, _, index, _ ->
            val intent = Intent(this, NewsWebviewActivity::class.java)
            intent.putExtra("link", newsList[index].link)
            startActivity(intent)
        }


    }
}

class RSSFeed() : AsyncTask<String?, Void?, String>() {
    override fun onPostExecute(s: String) {
        super.onPostExecute(s)
        val xmlParse = XMLParse()
        val document = xmlParse.getDocument(s) ?: return

        val nodeList = document.getElementsByTagName("item")
        var title = ""
        var link = ""
        var description = ""
        var imageLink = ""
        for (i in 0 until nodeList.length) {
            val element = nodeList.item(i) as Element
            title = """
                        ${xmlParse.getTitleValue(element, "title")}
                        
                        """.trimIndent()
            link = """
                        ${xmlParse.getTitleValue(element, "link")}
                        
                        """.trimIndent()
            description = "${xmlParse.getDescriptionValue(element, "description")}"
            imageLink = "${xmlParse.getImageLinkValue(element, "description")}"

            Log.e(TAG, "onPostExecute: $imageLink")
            newsList.add(News(title, description, link, imageLink))
            adapter?.notifyDataSetChanged()
        }
    }

    override fun doInBackground(vararg strings: String?): String {
        val content = StringBuilder()
        val url: URL
        try {
            url = URL(strings[0])
            val reader = InputStreamReader(url.openConnection().getInputStream())
            val bufferedReader = BufferedReader(reader)
            var line: String? = ""
            while (bufferedReader.readLine().also { line = it } != null) {
                content.append(line)
            }
            bufferedReader.close()
        } catch (e: Exception) {

        }


        return content.toString()
    }
}
