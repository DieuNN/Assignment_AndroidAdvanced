package com.example.assignmentandroidadvanced.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.database.Database
import com.example.assignmentandroidadvanced.database.SemesterDB
import com.example.assignmentandroidadvanced.databinding.ActivityCourseBinding

class CourseActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCourseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val semesterDB = SemesterDB(Database(this))
    }
}