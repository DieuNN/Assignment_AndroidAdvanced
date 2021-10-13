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
        holder.className.text = "Lớp: ${classList[position].name}"
        holder.semesterName.text = "Kỳ học: ${classList[position].semesterName}"
        holder.note.text = "Ghi chú: ${classList[position].note}"
        
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
                setTitle(mContext.getText(R.string.edit_class))
                setMessage(mContext.getText(R.string.spinner_hint))
                setNegativeButton(mContext.getText(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                setPositiveButton(mContext.getText(R.string.edit)) {_, _->
                    if(className.text.isNullOrBlank() || semesterName.isEmpty() || note.text.isNullOrBlank()) {
                        Toast.makeText(mContext, mContext.getText(R.string.information_must_not_empty), Toast.LENGTH_SHORT).show()
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
                setTitle(mContext.getText(R.string.delete_class))
                setNegativeButton(mContext.getText(R.string.cancel)) {dialog, _ ->
                    dialog.dismiss()
                }
                setPositiveButton(mContext.getText(R.string.delete)) {_, _ ->
                    if(ClassDB(Database(mContext)).removeClass(classList[position].name)) {
                        Toast.makeText(mContext, mContext.getText(R.string.delete_successfully), Toast.LENGTH_SHORT).show()
                        this@ClassAdapter.notifyItemRemoved(position)
                        classList.clear()
                        classList = ClassDB(Database(mContext)).getAllClasses()
                        this@ClassAdapter.notifyItemRangeChanged(position, classList.size)
                    } else {
                        Toast.makeText(mContext, mContext.getText(R.string.delete_failed), Toast.LENGTH_SHORT).show()
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
            Toast.makeText(mContext, mContext.getText(R.string.edit_successfully), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(mContext, mContext.getText(R.string.edit_failed), Toast.LENGTH_SHORT).show()
        }
    }
}