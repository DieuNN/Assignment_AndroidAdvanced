package com.example.assignmentandroidadvanced.dao

import com.example.assignmentandroidadvanced.model.Course

interface ICourse {
    fun newCourse(
        name: String,
        className: String,
        startDay: String,
        endDay: String,
        note: String
    ): Boolean

    fun editCourse(name: String,className: String, startDay: String, endDay: String, note: String): Boolean

    fun removeCourse(name: String): Boolean

    fun getAllCourse(): ArrayList<Course>
}