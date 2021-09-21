package com.example.assignmentandroidadvanced.database

import android.content.ContentValues
import com.example.assignmentandroidadvanced.dao.ISemester
import com.example.assignmentandroidadvanced.model.Semester

class SemesterDB(private val db:Database):ISemester {
    private val database = db.writableDatabase
    override fun newSemester(
        name: String,
        startDay: String,
        endDay: String,
        note: String
    ): Boolean {
        val contentValue = ContentValues()
        contentValue.apply {
            put("SEMESTER_NAME", name)
            put("START_DAY" , startDay)
            put("END_DAY", endDay)
            put("NOTE", note)
        }
        return database.insert(Database.TABLE_SEMESTER, null, contentValue) > 0
    }

    override fun editSemester(name: String, startDay: String, endDay: String, note: String): Boolean {
        val contentValue = ContentValues()
        contentValue.apply {
            put("SEMESTER_NAME", name)
            put("START_DAY" , startDay)
            put("END_DAY", endDay)
            put("NOTE", note)
        }
        return database.update(Database.TABLE_SEMESTER, contentValue, "SEMESTER_NAME = ?", arrayOf(name)) > 0
    }

    override fun removeSemester(name: String): Boolean {
        return database.delete(Database.TABLE_SEMESTER, "SEMESTER_NAME = ?", arrayOf(name)) > 0
    }

    override fun getAllSemesters(): ArrayList<Semester> {
        val result = ArrayList<Semester>()
        val cursor = database.rawQuery("SELECT * FROM ${Database.TABLE_SEMESTER}", null)

        if(cursor.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val name = cursor.getString(0)
                val startDay = cursor.getString(1)
                val endDay = cursor.getString(2)
                val note  = cursor.getString(3)

                val semester = Semester(name, startDay, endDay, note)

                result.add(semester)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return result
    }

}