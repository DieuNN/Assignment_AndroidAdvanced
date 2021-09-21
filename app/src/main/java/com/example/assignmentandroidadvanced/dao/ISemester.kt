package com.example.assignmentandroidadvanced.dao

import com.example.assignmentandroidadvanced.model.Semester

interface ISemester {
    fun newSemester(  name:String,  startDay:String,  endDay:String,  note:String):Boolean

    fun editSemester(name: String,startDay:String,  endDay:String,  note:String):Boolean

    fun removeSemester(name: String):Boolean

    fun getAllSemesters():ArrayList<Semester>
}