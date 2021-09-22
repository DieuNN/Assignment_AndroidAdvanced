package com.example.assignmentandroidadvanced.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.database.ClassDB
import com.example.assignmentandroidadvanced.database.Database
import com.example.assignmentandroidadvanced.database.SemesterDB
import com.example.assignmentandroidadvanced.model.Class
import com.google.android.material.textfield.TextInputEditText

class ClassAdapter(private val mContext: Context,private var classList:ArrayList<Class>) : RecyclerView.Adapter<ClassAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val className: TextView = itemView.findViewById(R.id.txtClassItemName)
        val semesterName:TextView = itemView.findViewById(R.id.txtClassItemSemesterName)
        val note:TextView= itemView.findViewById(R.id.txtClassItemNote)
        val btnEdit:ImageButton = itemView.findViewById(R.id.btnEditClass)
        val classItem:RelativeLayout = itemView.findViewById(R.id.relativeLayoutClassItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.class_item, null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.className.text = "Class: ${classList[position].name}"
        holder.semesterName.text = "Semester: ${classList[position].semesterName}"
        holder.note.text = classList[position].note
        
        holder.btnEdit.setOnClickListener {
            AlertDialog.Builder(mContext).apply {
                val view = LayoutInflater.from(mContext).inflate(R.layout.add_class_dialog, null)
                val className = view.findViewById<TextInputEditText>(R.id.edtClassName)
                var semesterName = view.findViewById<Spinner>(R.id.spinnerClassSemester)
                val note = view.findViewById<TextInputEditText>(R.id.edtClassNote)

                val semesterDB = SemesterDB(Database(mContext))
                val semesterList = mutableListOf<String>()
                for (element in semesterDB.getAllSemesters()) {
                    semesterList.add(element.name)
                }

                className.setText(classList[position].name)
                note.setText(classList[position].note)

                semesterName.adapter = ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, semesterList)

                setView(view)
                setTitle("Edit class!")
                setMessage("Hint:If no semester available, add semester and try again!")
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                setPositiveButton("Add") {_, _->
                    if(className.text.isNullOrBlank() || semesterName.isEmpty() || note.text.isNullOrBlank()) {
                        Toast.makeText(mContext, "Empty!", Toast.LENGTH_SHORT).show()
                    } else {
                        insertIntoDatabase(className.text.toString(), semesterName.selectedItem.toString(), note.text.toString())
                        classList.clear()
                        classList = ClassDB(Database(mContext)).getAllClasses()
                        this@ClassAdapter.notifyItemChanged(position)
                    }
                }
            }.create().show()
        }

        holder.classItem.setOnLongClickListener {
            AlertDialog.Builder(mContext).apply {
                setTitle("Delete class?")
                setNegativeButton("Cancel") {dialog, _ ->
                    dialog.dismiss()
                }
                setPositiveButton("OK") {_, _ ->
                    if(ClassDB(Database(mContext)).removeClass(classList[position].name)) {
                        Toast.makeText(mContext, "Removed!", Toast.LENGTH_SHORT).show()
                        this@ClassAdapter.notifyItemRemoved(position)
                        classList.clear()
                        classList = ClassDB(Database(mContext)).getAllClasses()
                        this@ClassAdapter.notifyItemRangeChanged(position, classList.size)
                    } else {
                        Toast.makeText(mContext, "Remove failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            }.create().show()
            true
        }
        
        
    }

    override fun getItemCount(): Int {
       return classList.size
    }

    private fun insertIntoDatabase(className:String, semesterName:String, note:String) {
        val classDB = ClassDB(Database(mContext))
        if(classDB.editClass(className, semesterName, note)) {
            Toast.makeText(mContext, "Edit successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(mContext, "Edit failed!", Toast.LENGTH_SHORT).show()
        }
    }
}