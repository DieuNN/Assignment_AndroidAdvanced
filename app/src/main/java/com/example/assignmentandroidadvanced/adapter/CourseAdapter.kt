package com.example.assignmentandroidadvanced.adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.database.ClassDB
import com.example.assignmentandroidadvanced.database.CourseDB
import com.example.assignmentandroidadvanced.database.Database
import com.example.assignmentandroidadvanced.model.Course
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.collections.ArrayList

class CourseAdapter(
    private val mContext: Context,
    private var courseList: ArrayList<Course>
) : RecyclerView.Adapter<CourseAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseName: TextView = itemView.findViewById(R.id.txtCourseItemName)
        val className: TextView = itemView.findViewById(R.id.txtCourseClassItemName)
        val startDate: TextView = itemView.findViewById(R.id.txtCourseItemStartDate)
        val endDate: TextView = itemView.findViewById(R.id.txtCourseItemEndDate)
        val note: TextView = itemView.findViewById(R.id.txtCourseItemNote)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnCourseItemEdit)
        val courseItem:RelativeLayout = itemView.findViewById(R.id.relativeLayoutCourseItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.course_item, null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.courseName.text = courseList[position].name
        holder.className.text = courseList[position].className
        holder.startDate.text = courseList[position].startDay
        holder.endDate.text = courseList[position].endDay
        holder.note.text = courseList[position].note

        holder.btnEdit.setOnClickListener {
            AlertDialog.Builder(mContext).apply {
                val view = LayoutInflater.from(mContext).inflate(R.layout.add_course_dialog, null)

                setView(view)

                val name = view.findViewById<TextView>(R.id.edtCourseName)
                val className = view.findViewById<Spinner>(R.id.spinnerClassName)
                val startDate = view.findViewById<TextView>(R.id.edtCourseStartDate)
                val endDate = view.findViewById<TextView>(R.id.edtCourseEndDate)
                val note = view.findViewById<TextView>(R.id.edtCourseNote)

                val classDB = ClassDB(Database(mContext))
                val classNameList = mutableListOf<String>()
                for (element in classDB.getAllClasses()) {
                    classNameList.add(element.name)
                }
                className.adapter = ArrayAdapter(mContext, android.R.layout.simple_dropdown_item_1line, classNameList)


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

                name.text = courseList[position].name
                startDate.text = courseList[position].startDay
                endDate.text = courseList[position].endDay
                note.text = courseList[position].note

                setMessage("Edit Course!")
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                setPositiveButton("OK") { _, _ ->
                    insertIntoDatabase(
                        name.text.toString(),
                        className?.selectedItem.toString(),
                        startDate.text.toString(),
                        endDate.text.toString(),
                        note.text.toString()
                    )
                    courseList.clear()
                    courseList = CourseDB(Database(mContext)).getAllCourse()
                    this@CourseAdapter.notifyDataSetChanged()
                }
            }.create().show()
        }

        holder.courseItem.setOnLongClickListener {
            AlertDialog.Builder(mContext).apply {
                setTitle("Delete course?")
                setNegativeButton("Cancel") {dialog, _ ->
                    dialog.dismiss()
                }
                setPositiveButton("OK") {_, _ ->
                    val courseDB = CourseDB(Database(mContext))
                    if(courseDB.removeCourse(courseList[position].name)) {
                        Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show()
                        this@CourseAdapter.notifyItemRemoved(position)
                        courseList.clear()
                        courseList = courseDB.getAllCourse()
                        this@CourseAdapter.notifyItemRangeChanged(position, courseList.size)
                    } else {
                        Toast.makeText(mContext, "Delete failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            }.create().show()
            true
        }
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    private fun insertIntoDatabase(name: String,className:String, startDay: String, endDay: String, note: String) {
        val courseDB = CourseDB(Database(mContext))
        if (courseDB.editCourse(name,className, startDay, endDay, note)) {
            Toast.makeText(mContext, "Edit successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                mContext,
                "Edit failed! You changed course name? Remove it and add it again!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}