package com.example.assignmentandroidadvanced.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.adapter.SemesterListAdapter
import com.example.assignmentandroidadvanced.database.Database
import com.example.assignmentandroidadvanced.database.SemesterDB
import com.example.assignmentandroidadvanced.databinding.FragmentSemesterBinding
import com.example.assignmentandroidadvanced.model.Semester
import com.google.android.material.textfield.TextInputEditText

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

        var listSemester = ArrayList<Semester>()
        listSemester.clear()
        listSemester = semesterDB.getAllSemesters()

        binding.listSemester.adapter = SemesterListAdapter(requireContext(), listSemester)

        binding.fabAddSemester.setOnClickListener {
            addSemester()
        }
    }

    private fun addSemester() {
        AlertDialog.Builder(requireContext()).apply {
            setMessage("Add new semester!")
            val view =
                LayoutInflater.from(requireContext()).inflate(R.layout.add_semester_dialog, null)
            setView(view)
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Add") { _, _ ->
                val name: String =
                    view.findViewById<TextInputEditText>(R.id.edtSemesterName).text.toString()
                val startDate =
                    view.findViewById<TextInputEditText>(R.id.edtSemesterStartDate).text.toString()
                val endDate =
                    view.findViewById<TextInputEditText>(R.id.edtSemesterEndDate).text.toString()
                val note =
                    view.findViewById<TextInputEditText>(R.id.edtSemesterNote).text.toString()

                if(name.isBlank() || startDate.isBlank() || endDate.isBlank() || note.isBlank()) {
                    Toast.makeText(requireContext(), "Empty!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                insertIntoDatabase(name, startDate, endDate, note)
            }

        }.create().show()
    }

    private fun controlIsEmptyText() {
        if (semesterDB.getAllSemesters().isEmpty()) {
            binding.txtIsSemesterEmpty.visibility = View.VISIBLE
        } else {
            binding.txtIsSemesterEmpty.visibility = View.INVISIBLE
        }
    }

    private fun insertIntoDatabase(name: String, startDate: String, endDate: String, note: String) {
        if (semesterDB.newSemester(name, startDate, endDate, note)) {
            Toast.makeText(requireContext(), "Added successfully!", Toast.LENGTH_SHORT).show()
            setupListView()
            controlIsEmptyText()
        } else {
            Toast.makeText(requireContext(), "Semester has already added", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupListView() {
        var listSemester = ArrayList<Semester>()
        listSemester.clear()
        listSemester = semesterDB.getAllSemesters()

        binding.listSemester.adapter = SemesterListAdapter(requireContext(), listSemester)
        controlIsEmptyText()
    }
}