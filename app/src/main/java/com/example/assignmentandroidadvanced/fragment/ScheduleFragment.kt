package com.example.assignmentandroidadvanced.fragment

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.adapter.ScheduleAdapter
import com.example.assignmentandroidadvanced.adapter.SemesterAdapter
import com.example.assignmentandroidadvanced.database.*
import com.example.assignmentandroidadvanced.databinding.FragmentScheduleBinding
import com.example.assignmentandroidadvanced.model.Schedule
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

        setupListView()
        controlIsEmptyText()
        binding.fabAddSchedule.setOnClickListener {
            addSchedule()
        }
    }


    private fun controlIsEmptyText() {
        if (scheduleDB.getAllSchedule().isEmpty()) {
            binding.txtIsScheduleEmpty.visibility = View.VISIBLE
        } else  {
            binding.txtIsScheduleEmpty.visibility = View.INVISIBLE
        }
    }

    private fun addSchedule() {
        AlertDialog.Builder(requireContext()).apply {
            setMessage("Add new semester!")
            val view =
                LayoutInflater.from(requireContext()).inflate(R.layout.add_schedule_dialog, null)

            val semesterName = view.findViewById<Spinner>(R.id.spinnerScheduleSemesterName)
            val className = view.findViewById<Spinner>(R.id.spinnerScheduleClassName)
            val courseName = view.findViewById<Spinner>(R.id.spinnerScheduleCourseName)
            val startTime = view.findViewById<TextInputEditText>(R.id.edtScheduleStartTime)
            val endTime = view.findViewById<TextInputEditText>(R.id.edtScheduleEndTime)
            val note = view.findViewById<TextInputEditText>(R.id.edtScheduleNote)

            val semesterList = mutableListOf<String>()
            for (element in SemesterDB(Database(requireContext())).getAllSemesters()) {
                semesterList.add(element.name)
            }
            semesterName.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, semesterList)

            val classList = mutableListOf<String>()
            for (element in ClassDB(Database(requireContext())).getAllClasses()) {
                classList.add(element.name)
            }
            className.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, classList)

            val courseList = mutableListOf<String>()
            for(element in CourseDB(Database(requireContext())).getAllCourse()) {
                courseList.add(element.name)
            }
            courseName.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, courseList)



            val startTimeLayout =
                view.findViewById<TextInputLayout>(R.id.edtLayoutScheduleStartTime)
            val endTimeLayout =
                view.findViewById<TextInputLayout>(R.id.edtLayoutScheduleEndTime)


            startTimeLayout.setEndIconOnClickListener {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    startTime.setText(SimpleDateFormat("HH:mm").format(calendar.time))
                }
                TimePickerDialog(requireContext(), timeSetListener, hour, minute, true).show()

            }

            endTimeLayout.setEndIconOnClickListener {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    endTime.setText(SimpleDateFormat("HH:mm").format(calendar.time))
                }
                TimePickerDialog(requireContext(), timeSetListener, hour, minute, true).show()
            }


            setView(view)
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Add") { _, _ ->
                if (endTime.text.isNullOrBlank() || startTime.text.isNullOrBlank() || note.text.isNullOrBlank()) {
                    Toast.makeText(requireContext(), "Start time, end time or note is blank", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                } else {
//                    Toast.makeText(requireContext(), "${semesterName.selectedItem.toString()}, ${className.selectedItem.toString()}, ${courseName.selectedItem.toString()} ,${startTime.text.toString()}, ${endTime.text.toString()}, ${note.text.toString()}", Toast.LENGTH_SHORT).show()
                    insertIntoDatabase(semesterName.selectedItem.toString(), className.selectedItem.toString(), courseName.selectedItem.toString() ,startTime.text.toString(), endTime.text.toString(), note.text.toString())
                    setupListView()
                }
            }

        }.create().show()
    }

    private fun insertIntoDatabase(semesterName:String, className:String,courseName:String, fromHour:String, toHour:String, note:String) {
        val scheduleDB = ScheduleDB(Database(requireContext()))
        if(scheduleDB.newSchedule(null, semesterName = semesterName, className = className, courseName = courseName, fromHour = fromHour, toHour = toHour, note = note)) {
            Toast.makeText(requireContext(), "Add successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Add failed!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupListView() {
        binding.listSchedule.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.listSchedule.layoutManager = LinearLayoutManager(requireContext())
        var scheduleList = ArrayList<Schedule>()
        scheduleList.clear()
        scheduleList = scheduleDB.getAllSchedule()
        binding.listSchedule.adapter =
            ScheduleAdapter(requireContext(), scheduleList)
    }

}