package com.example.assignmentandroidadvanced.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.database.CourseDB
import com.example.assignmentandroidadvanced.database.Database
import com.example.assignmentandroidadvanced.databinding.FragmentCourseBinding

class CourseFragment : Fragment() {
    private lateinit var binding:FragmentCourseBinding
    private lateinit var courseDB: CourseDB
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCourseBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        courseDB = CourseDB(Database(requireContext()))

        controlIsEmptyText()
    }

    private fun controlIsEmptyText() {
        if (courseDB.getAllCourse().isEmpty()) {
            binding.txtIsCourseListEmpty.visibility = View.VISIBLE
        } else  {
            binding.txtIsCourseListEmpty.visibility = View.INVISIBLE
        }
    }

}