package com.example.assignmentandroidadvanced.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.adapter.ClassAdapter
import com.example.assignmentandroidadvanced.adapter.SemesterAdapter
import com.example.assignmentandroidadvanced.database.ClassDB
import com.example.assignmentandroidadvanced.database.Database
import com.example.assignmentandroidadvanced.database.SemesterDB
import com.example.assignmentandroidadvanced.databinding.FragmentClassBinding
import com.example.assignmentandroidadvanced.model.Class
import com.google.android.material.textfield.TextInputEditText
import java.util.ArrayList


class ClassFragment : Fragment() {

    private lateinit var binding:FragmentClassBinding
    private lateinit var classDB: ClassDB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClassBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        classDB = ClassDB(Database(requireContext()))

        controlIsEmptyText()
        setupListView()

        binding.fabAddClass.setOnClickListener {
            addClass()
        }
    }

    private fun setupListView() {
        binding.listClass.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.listClass.layoutManager = LinearLayoutManager(requireContext())
        var classList = ArrayList<Class>()
        classList.clear()
        classList = classDB.getAllClasses()
        binding.listClass.adapter =
            ClassAdapter(requireContext(), classList)
    }

    private fun controlIsEmptyText() {
        if (classDB.getAllClasses().isEmpty()) {
            binding.txtIsClassListEmpty.visibility = View.VISIBLE
        } else  {
            binding.txtIsClassListEmpty.visibility = View.INVISIBLE
        }
    }

    private fun addClass() {
        AlertDialog.Builder(requireContext()).apply {
            val view = LayoutInflater.from(requireContext()).inflate(R.layout.add_class_dialog, null)
            val className = view.findViewById<TextInputEditText>(R.id.edtClassName)
            val semesterName = view.findViewById<Spinner>(R.id.spinnerClassSemester)
            val note = view.findViewById<TextInputEditText>(R.id.edtClassNote)

            val semesterDB = SemesterDB(Database(requireContext()))
            val semesterList = mutableListOf<String>()
            for (element in semesterDB.getAllSemesters()) {
                semesterList.add(element.name)
            }
            semesterName.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, semesterList)

            setView(view)
            setTitle("Add class!")
            setMessage("Hint:If no semester available, add semester and try again!")
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Add") {_, _->
                if(className.text.isNullOrBlank() || semesterName.isEmpty() || note.text.isNullOrBlank()) {
                    Toast.makeText(requireContext(), "Empty!", Toast.LENGTH_SHORT).show()
                } else {
                    insertIntoDatabase(className.text.toString(), semesterName.selectedItem.toString(), note.text.toString())
                    setupListView()
                }
            }
        }.create().show()
    }

    private fun insertIntoDatabase(className:String, semesterName:String, note:String) {
        val classDB = ClassDB(Database(requireContext()))
        if(classDB.newClass(className, semesterName, note)) {
            Toast.makeText(requireContext(), "Add successfully!", Toast.LENGTH_SHORT).show()
            controlIsEmptyText()
        } else {
            Toast.makeText(requireContext(), "Add successfully!", Toast.LENGTH_SHORT).show()
        }
    }


}