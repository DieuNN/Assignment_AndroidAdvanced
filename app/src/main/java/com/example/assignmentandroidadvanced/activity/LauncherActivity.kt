package com.example.assignmentandroidadvanced.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.databinding.ActivityLauncherBinding

class LauncherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLauncherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 1500)
    }
}