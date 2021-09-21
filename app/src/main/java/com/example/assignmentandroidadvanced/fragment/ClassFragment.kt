package com.example.assignmentandroidadvanced.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.database.ClassDB
import com.example.assignmentandroidadvanced.database.Database
import com.example.assignmentandroidadvanced.databinding.FragmentClassBinding


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

    }

    private fun controlIsEmptyText() {
        if (classDB.getAllClasses().isEmpty()) {
            binding.txtIsClassListEmpty.visibility = View.VISIBLE
        } else  {
            binding.txtIsClassListEmpty.visibility = View.INVISIBLE
        }
    }



}