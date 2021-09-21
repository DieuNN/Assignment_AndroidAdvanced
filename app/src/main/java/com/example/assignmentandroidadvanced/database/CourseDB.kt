package com.example.assignmentandroidadvanced.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.assignmentandroidadvanced.dao.ICourse
import com.example.assignmentandroidadvanced.model.Course

class CourseDB(private val db: Database) : ICourse {
    private val database: SQLiteDatabase = db.writableDatabase
    override fun newCourse(
        name: String,
        className: String,
        startDay: String,
        endDay: String,
        note: String
    ): Boolean {

        val contentValue = ContentValues()
        contentValue.apply {
            put("COURSE_NAME", name)
            put("CLASS_NAME", className)
            put("START_DAY", startDay)
            put("END_DAY", endDay)
            put("NOTE", note)
        }

        return database.insert(Database.TABLE_COURSE, null, contentValue) > 0

    }

    override fun editCourse(
        name: String,
        className: String,
        startDay: String,
        endDay: String,
        note: String
    ): Boolean {
        val contentValue = ContentValues()
        contentValue.apply {
            put("COURSE_NAME", name)
            put("CLASS_NAME", className)
            put("START_DAY", startDay)
            put("END_DAY", endDay)
            put("NOTE", note)
        }

        return database.update(Database.TABLE_COURSE, contentValue, "COURSE_NAME = ?", arrayOf(name)) > 0
    }

    override fun removeCourse(name: String): Boolean {
        return database.delete(Database.TABLE_COURSE, "COURSE_NAME = ?", arrayOf(name)) > 0
    }

    override fun getAllCourse(): ArrayList<Course> {
        val result: ArrayList<Course> = ArrayList()
        val cursor = database.rawQuery("SELECT * FROM ${Database.TABLE_COURSE}", null)

        if(cursor.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val name = cursor.getString(0)
                val className = cursor.getString(1)
                val startDay = cursor.getString(2)
                val endDay = cursor.getString(3)
                val note = cursor.getString(4)
                val course = Course(name, className, startDay, endDay, note)

                result.add(course)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return result
    }

}