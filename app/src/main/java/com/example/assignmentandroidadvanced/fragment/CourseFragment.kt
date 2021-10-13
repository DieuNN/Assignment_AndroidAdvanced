package com.example.assignmentandroidadvanced.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.adapter.CourseAdapter
import com.example.assignmentandroidadvanced.database.ClassDB
import com.example.assignmentandroidadvanced.database.CourseDB
import com.example.assignmentandroidadvanced.database.Database
import com.example.assignmentandroidadvanced.databinding.FragmentCourseBinding
import com.example.assignmentandroidadvanced.model.Course
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.collections.ArrayList

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
        setupListView()

        binding.fabAddCourse.setOnClickListener {
            addCourse()
        }
    }

    private fun controlIsEmptyText() {
        if (courseDB.getAllCourse().isEmpty()) {
            binding.txtIsCourseListEmpty.visibility = View.VISIBLE
        } else  {
            binding.txtIsCourseListEmpty.visibility = View.INVISIBLE
        }
    }

    private fun addCourse() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(requireContext().getText(R.string.add_course))
            setMessage(requireContext().getText(R.string.spinner_hint))
            val view =
                LayoutInflater.from(requireContext()).inflate(R.layout.add_course_dialog, null)

            val name = view.findViewById<TextView>(R.id.edtCourseName)
            val className = view.findViewById<Spinner>(R.id.spinnerClassName)
            val startDate = view.findViewById<TextView>(R.id.edtCourseStartDate)
            val endDate = view.findViewById<TextView>(R.id.edtCourseEndDate)
            val note = view.findViewById<TextView>(R.id.edtCourseNote)


            val classDB = ClassDB(Database(requireContext()))
            val classNameList = mutableListOf<String>()
            for (element in classDB.getAllClasses()) {
                classNameList.add(element.name)
            }
            className.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, classNameList)

            val startDateLayout =
                view.findViewById<TextInputLayout>(R.id.edtLayoutCourseStartDate)
            val endDateLayout =
                view.findViewById<TextInputLayout>(R.id.edtLayoutCourseEndDate)

            startDateLayout.setEndIconOnClickListener {
                val calendar = Calendar.getInstance()
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)

                // New date picker dialog
                val datePickerDialog =
                    DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                        let {
                            startDate.setText("$dayOfMonth/${month + 1}/$year")
                        }
                    }, year, month, day)
                datePickerDialog.show()

            }

            endDateLayout.setEndIconOnClickListener {
                val calendar = Calendar.getInstance()
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)

                // New date picker dialog
                val datePickerDialog =
                    DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                        let {
                            endDate.setText("$dayOfMonth/${month + 1}/$year")
                        }
                    }, year, month, day)
                datePickerDialog.show()
            }

            setView(view)
            setNegativeButton(requireContext().getText(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton(requireContext().getText(R.string.add)) { _, _ ->
                if (name.text!!.isBlank() || startDate.text!!.isBlank() || endDate.text!!.isBlank() || note.text!!.isBlank()) {
                    Toast.makeText(requireContext(), requireContext().getText(R.string.information_must_not_empty), Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if(className.selectedItem == null) {
                    Toast.makeText(requireContext(), requireContext().getText(R.string.information_must_not_empty), Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                insertIntoDatabase(
                    name.text.toString(),
                    className?.selectedItem.toString(),
                    startDate.text.toString(),
                    endDate.text.toString(),
                    note.text.toString()
                )
            }

        }.create().show()
    }

    private fun setupListView() {
        binding.listCourse.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.listCourse.layoutManager = LinearLayoutManager(requireContext())
        var courseList = ArrayList<Course>()
        courseList.clear()
        courseList = courseDB.getAllCourse()
        binding.listCourse.adapter =
            CourseAdapter(requireContext(), courseList)
    }

    private fun insertIntoDatabase(name: String,className:String    , startDate: String, endDate: String, note: String) {
        if (courseDB.newCourse(name,className, startDate, endDate, note)) {
            Toast.makeText(requireContext(), requireContext().getText(R.string.add_successfully), Toast.LENGTH_SHORT).show()
            controlIsEmptyText()
            setupListView()
        } else {
            Toast.makeText(requireContext(), requireContext().getText(R.string.add_failed), Toast.LENGTH_SHORT)
                .show()
        }
    }
}