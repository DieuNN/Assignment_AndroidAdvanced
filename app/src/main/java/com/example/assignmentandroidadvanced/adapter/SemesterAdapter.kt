package com.example.assignmentandroidadvanced.adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.database.Database
import com.example.assignmentandroidadvanced.database.SemesterDB
import com.example.assignmentandroidadvanced.model.Class
import com.example.assignmentandroidadvanced.model.Schedule
import com.example.assignmentandroidadvanced.model.Semester
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.collections.ArrayList

class SemesterAdapter(
    private val mContext: Context,
    private var semesterList: ArrayList<Semester>
) : RecyclerView.Adapter<SemesterAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txtSemesterItemName)
        val startDate: TextView = itemView.findViewById(R.id.txtSemesterItemStartDate)
        val endDate: TextView = itemView.findViewById(R.id.txtSemesterItemEndDate)
        val note: TextView = itemView.findViewById(R.id.txtSemesterItemNote)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnSemesterItemEdit)
        val item = itemView.findViewById<RelativeLayout>(R.id.relLayoutSemesterItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SemesterAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.semester_item, null))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: SemesterAdapter.ViewHolder, position: Int) {
        holder.name.text = "Name: ${semesterList[position].name}"
        holder.startDate.text = "Start day:${semesterList[position].startDay} "
        holder.endDate.text = "End day: ${semesterList[position].endDay}"
        holder.note.text = "Note: ${semesterList[position].note}"

        holder.btnEdit.setOnClickListener {
            AlertDialog.Builder(mContext).apply {
                val view = LayoutInflater.from(mContext).inflate(R.layout.add_semester_dialog, null)

                setView(view)

                val name = view.findViewById<TextView>(R.id.edtSemesterName)
                val startDate = view.findViewById<TextView>(R.id.edtSemesterStartDate)
                val endDate = view.findViewById<TextView>(R.id.edtSemesterEndDate)
                val note = view.findViewById<TextView>(R.id.edtSemesterNote)



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
                        DatePickerDialog(mContext, { _, year, month, dayOfMonth ->
                            let {
                                startDate.text = "$dayOfMonth/${month + 1}/$year"
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
                        DatePickerDialog(mContext, { _, year, month, dayOfMonth ->
                            let {
                                endDate.text = "$dayOfMonth/${month + 1}/$year"
                            }
                        }, year, month, day)
                    datePickerDialog.show()
                }

                name.text = semesterList[position].name
                startDate.text = semesterList[position].startDay
                endDate.text = semesterList[position].endDay
                note.text = semesterList[position].note

                setMessage("Edit semester")
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                setPositiveButton("OK") { _, _ ->
                    insertIntoDatabase(
                        name.text.toString(),
                        startDate.text.toString(),
                        endDate.text.toString(),
                        note.text.toString()
                    )
                    semesterList.clear()
                    semesterList = SemesterDB(Database(mContext)).getAllSemesters()
                    this@SemesterAdapter.notifyDataSetChanged()
                }
            }.create().show()
        }

        holder.item.setOnLongClickListener {
            val semesterDB = SemesterDB(Database(mContext))
            AlertDialog.Builder(mContext).apply {
                setMessage("Delete this semester?")
                setNegativeButton("Cancel") {dialog, _ ->
                    dialog.dismiss()
                }
                setPositiveButton("Delete") {_,_ ->
                    if (semesterDB.removeSemester(semesterList[position].name)) {
                        Toast.makeText(mContext, "Delete successfully", Toast.LENGTH_SHORT).show()
                        // notify item removed
                        this@SemesterAdapter.notifyItemRemoved(position)
                        semesterList.clear()
                        semesterList = semesterDB.getAllSemesters()
                        // notify range changed, if not it will be IndexOutOfBoundException
                        this@SemesterAdapter.notifyItemRangeChanged(position, semesterList.size)
                    } else {
                        Toast.makeText(mContext, "Delete failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            }.create().show()
            true
        }

    }

    override fun getItemCount(): Int {
        return semesterList.size
    }

    private fun insertIntoDatabase(name: String, startDay: String, endDay: String, note: String) {
        val semesterDB = SemesterDB(Database(mContext))
        if (semesterDB.editSemester(name, startDay, endDay, note)) {
            Toast.makeText(mContext, "Edit successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                mContext,
                "Edit failed! You changed semester name? Remove it and add it again!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}