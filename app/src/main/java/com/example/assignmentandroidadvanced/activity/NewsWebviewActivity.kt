package com.example.assignmentandroidadvanced.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.assignmentandroidadvanced.R

class NewsWebviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_webview)

        findViewById<WebView>(R.id.webViewNews).apply {
            loadUrl(intent.getStringExtra("link")!!)
            webViewClient = WebViewClient()
        }
    }
}