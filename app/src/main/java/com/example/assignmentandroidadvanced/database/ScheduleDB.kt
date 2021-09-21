package com.example.assignmentandroidadvanced.database

import android.content.ContentValues
import com.example.assignmentandroidadvanced.dao.ISchedule
import com.example.assignmentandroidadvanced.model.Schedule

class ScheduleDB(private val db:Database): ISchedule {
    private val database = db.writableDatabase
    override fun newSchedule(
        id: Int,
        semesterName: String,
        className: String,
        courseName: String,
        fromHour: String,
        toHour: String,
        note: String
    ): Boolean {
        val contentValues = ContentValues()
        contentValues.apply {
            put("ID", id)
            put("SEMESTER_NAME", semesterName)
            put("CLASS_NAME", className)
            put("COURSE_NAME", courseName)
            put("FROM_HOUR", fromHour)
            put("TO_HOUR", toHour)
            put("NOTE", note)
        }
        return database.insert(Database.TABLE_SCHEDULE, null, contentValues) > 0
    }

    override fun editSchedule(
        id: Int,
        semesterName: String,
        className: String,
        courseName: String,
        fromHour: String,
        toHour: String,
        note: String
    ): Boolean {
        val contentValues = ContentValues()
        contentValues.apply {
            put("ID", id)
            put("SEMESTER_NAME", semesterName)
            put("CLASS_NAME", className)
            put("COURSE_NAME", courseName)
            put("FROM_HOUR", fromHour)
            put("TO_HOUR", toHour)
            put("NOTE", note)
        }
        return database.update(Database.TABLE_SCHEDULE, contentValues, "ID = ?", arrayOf(id.toString())) > 0
    }

    override fun removeSchedule(id: Int):Boolean {
        return database.delete(Database.TABLE_SCHEDULE, "ID = ?", arrayOf(id.toString())) > 0
    }

    override fun getAllSchedule(): ArrayList<Schedule> {
        val result = ArrayList<Schedule>()
        val cursor = database.rawQuery("SELECT * FROM ${Database.TABLE_SCHEDULE}", null)

        if(cursor.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val id = cursor.getInt(0)
                val semesterName = cursor.getString(1)
                val className = cursor.getString(2)
                val courseName = cursor.getString(3)
                val fromHour = cursor.getString(4)
                val toHour = cursor.getString(5)
                val note = cursor.getString(6)

                val schedule = Schedule(id, semesterName, className, courseName, fromHour, toHour, note)
                result.add(schedule)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return result
    }
}