package com.example.assignmentandroidadvanced.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.databinding.ActivityCourseBinding
import com.example.assignmentandroidadvanced.fragment.ClassFragment
import com.example.assignmentandroidadvanced.fragment.CourseFragment
import com.example.assignmentandroidadvanced.fragment.ScheduleFragment
import com.example.assignmentandroidadvanced.fragment.SemesterFragment

class CourseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.frameLayoutCourse, ScheduleFragment()).commit()


        binding.bottomNavigationCourse.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.menu_schedule -> supportFragmentManager.beginTransaction().replace(R.id.frameLayoutCourse, ScheduleFragment()).commit()
                R.id.menu_class -> supportFragmentManager.beginTransaction().replace(R.id.frameLayoutCourse, ClassFragment()).commit()
                R.id.menu_course -> supportFragmentManager.beginTransaction().replace(R.id.frameLayoutCourse, CourseFragment()).commit()
                R.id.menu_semester -> supportFragmentManager.beginTransaction().replace(R.id.frameLayoutCourse, SemesterFragment()).commit()
            }
            true
        }
    }
}