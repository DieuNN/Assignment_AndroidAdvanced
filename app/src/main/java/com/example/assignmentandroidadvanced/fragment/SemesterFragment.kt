package com.example.assignmentandroidadvanced.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.adapter.SemesterAdapter
import com.example.assignmentandroidadvanced.database.Database
import com.example.assignmentandroidadvanced.database.SemesterDB
import com.example.assignmentandroidadvanced.databinding.FragmentSemesterBinding
import com.example.assignmentandroidadvanced.model.Semester
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class SemesterFragment : Fragment() {
    private lateinit var binding: FragmentSemesterBinding
    private lateinit var semesterDB: SemesterDB
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSemesterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        semesterDB = SemesterDB(Database(requireContext()))

        controlIsEmptyText()
        setupListView()


        binding.fabAddSemester.setOnClickListener {
            addSemester()
        }
    }

    private fun addSemester() {
        AlertDialog.Builder(requireContext()).apply {
            setMessage("Add new semester!")
            val view =
                LayoutInflater.from(requireContext()).inflate(R.layout.add_semester_dialog, null)

            val name =
                view.findViewById<TextInputEditText>(R.id.edtSemesterName)
            val startDate =
                view.findViewById<TextInputEditText>(R.id.edtSemesterStartDate)
            val endDate =
                view.findViewById<TextInputEditText>(R.id.edtSemesterEndDate)
            val note =
                view.findViewById<TextInputEditText>(R.id.edtSemesterNote)
            val startDateLayout =
                view.findViewById<TextInputLayout>(R.id.edtLayoutSemesterStartDate)
            val endDateLayout =
                view.findViewById<TextInputLayout>(R.id.edtLayoutSemesterEndDate)

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
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Add") { _, _ ->
                if (name.text!!.isBlank() || startDate.text!!.isBlank() || endDate.text!!.isBlank() || note.text!!.isBlank()) {
                    Toast.makeText(requireContext(), "Empty!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                insertIntoDatabase(
                    name.text.toString(),
                    startDate.text.toString(),
                    endDate.text.toString(),
                    note.text.toString()
                )
            }

        }.create().show()
    }

    companion object fun controlIsEmptyText() {
        if (semesterDB.getAllSemesters().isEmpty()) {
            binding.txtIsSemesterEmpty.visibility = View.VISIBLE
        } else {
            binding.txtIsSemesterEmpty.visibility = View.INVISIBLE
        }
    }

    private fun insertIntoDatabase(name: String, startDate: String, endDate: String, note: String) {
        if (semesterDB.newSemester(name, startDate, endDate, note)) {
            Toast.makeText(requireContext(), "Added successfully!", Toast.LENGTH_SHORT).show()
            controlIsEmptyText()
            setupListView()
        } else {
            Toast.makeText(requireContext(), "Semester has already added", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun setupListView() {
        binding.listSemester.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.listSemester.layoutManager = LinearLayoutManager(requireContext())
        var semesterList = ArrayList<Semester>()
        semesterList.clear()
        semesterList = semesterDB.getAllSemesters()
        binding.listSemester.adapter =
            SemesterAdapter(requireContext(), semesterList)
    }
}