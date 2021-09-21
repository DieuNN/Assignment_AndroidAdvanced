package com.example.assignmentandroidadvanced.dao

import com.example.assignmentandroidadvanced.model.Class

interface IClass {
    fun newClass(  name:String,  semesterName:String,  note:String):Boolean

    fun editClass(name:String ,semesterName:String,  note:String):Boolean

    fun removeClass(name: String):Boolean

    fun getAllClasses():ArrayList<Class>
}