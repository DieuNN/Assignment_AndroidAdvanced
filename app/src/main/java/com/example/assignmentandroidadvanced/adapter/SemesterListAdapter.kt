package com.example.assignmentandroidadvanced.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.model.Semester

class SemesterListAdapter(private val mContext:Context, val semesterList:ArrayList<Semester>) :ArrayAdapter<Semester>(mContext, 0) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.semester_item, null)

        val name = view.findViewById<TextView>(R.id.txtSemesterItemName)
        val startDate = view.findViewById<TextView>(R.id.txtSemesterItemStartDate)
        val endDate = view.findViewById<TextView>(R.id.txtSemesterItemEndDate)
        val note = view.findViewById<TextView>(R.id.txtSemesterItemNote)
        val editButton = view.findViewById<ImageButton>(R.id.btnSemesterItemEdit)

        name.text = semesterList[position].name
        startDate.text = semesterList[position].startDay
        endDate.text = semesterList[position].endDay
        note.text = semesterList[position].note


        editButton.setOnClickListener {
            Toast.makeText(mContext, "Let be tomorrow", Toast.LENGTH_SHORT).show()
        }

        return view

    }
}