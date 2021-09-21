package com.example.assignmentandroidadvanced.fragment

import android.os.Bundle
import android.service.controls.templates.ControlButton
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.database.Database
import com.example.assignmentandroidadvanced.database.ScheduleDB
import com.example.assignmentandroidadvanced.databinding.ActivityCourseBinding.inflate
import com.example.assignmentandroidadvanced.databinding.FragmentScheduleBinding

class ScheduleFragment : Fragment() {
    private lateinit var binding: FragmentScheduleBinding
    private lateinit var scheduleDB:ScheduleDB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scheduleDB = ScheduleDB(Database(requireContext()))

        controlIsEmptyText()
    }


    private fun controlIsEmptyText() {
        if (scheduleDB.getAllSchedule().isEmpty()) {
            binding.txtIsScheduleEmpty.visibility = View.VISIBLE
        } else  {
            binding.txtIsScheduleEmpty.visibility = View.INVISIBLE
        }
    }


}