package com.example.assignmentandroidadvanced.dao

import com.example.assignmentandroidadvanced.model.Course
import com.example.assignmentandroidadvanced.model.Schedule

interface ISchedule {
    //data class Schedule(val Id:Int, val semesterName:String, val className:String, val courseName:String, val note:String)
    fun newSchedule(id:Int, semesterName:String, className:String, courseName:String, fromHour:String, toHour:String,  note:String):Boolean

    fun editSchedule(id:Int, semesterName:String, className:String, courseName:String,fromHour:String, toHour:String, note:String):Boolean

    fun removeSchedule(id: Int):Boolean

    fun getAllSchedule():ArrayList<Schedule>

}