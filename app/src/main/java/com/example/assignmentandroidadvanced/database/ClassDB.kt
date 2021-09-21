package com.example.assignmentandroidadvanced.database

import android.content.ContentValues
import com.example.assignmentandroidadvanced.dao.IClass
import com.example.assignmentandroidadvanced.model.Class

class ClassDB(private val db:Database):IClass {
    private val database = db.writableDatabase
    override fun newClass(name: String, semesterName: String, note: String): Boolean {
        val contentValue = ContentValues()
        contentValue.apply {
            put("CLASS_NAME", name)
            put("SEMESTER_NAME", semesterName)
            put("NOTE", note)
        }
        return database.insert(Database.TABLE_CLASS, null, contentValue) > 0
    }

    override fun editClass(name:String, semesterName: String, note: String): Boolean {
        val contentValue = ContentValues()
        contentValue.apply {
            put("CLASS_NAME", name)
            put("SEMESTER_NAME", semesterName)
            put("NOTE", note)
        }
        return database.update(Database.TABLE_CLASS, null, "CLASS_NAME", arrayOf(name)) > 0
    }

    override fun removeClass(name: String): Boolean {
        return database.delete(Database.TABLE_CLASS, "CLASS_NAME", arrayOf(name)) > 0
    }

    override fun getAllClasses(): ArrayList<Class> {
        val result :ArrayList<Class> = ArrayList()
        val cursor = database.rawQuery("SELECT * FROM ${Database.TABLE_CLASS}", null)

        if (cursor.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val name = cursor.getString(0)
                val semesterName = cursor.getString(1)
                val note = cursor.getString(2)

                val mClass = Class(name, semesterName, note)

                result.add(mClass)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return result
    }
}