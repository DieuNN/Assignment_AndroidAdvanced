package com.example.assignmentandroidadvanced.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(private val mContext: Context) :
    SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "COURSE_MANAGEMENT"
        const val DATABASE_VERSION = 1
        const val TABLE_CLASS = "TABLE_CLASS"
        const val TABLE_COURSE = "TABLE_COURSE"
        const val TABLE_SEMESTER = "TABLE_SEMESTER"
        const val TABLE_SCHEDULE = "TABLE_SCHEDULE"
    }

    // Enable foreign key
    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        db?.execSQL("PRAGMA foreign_keys = ON")
    }

    // ON DELETE CASCADE: when delete element in parent table, child element references to parent will be deleted
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_SEMESTER (SEMESTER_NAME TEXT PRIMARY KEY, START_DAY TEXT, END_DAY TEXT, NOTE TEXT)")
        db.execSQL("CREATE TABLE $TABLE_CLASS (CLASS_NAME PRIMARY KEY, SEMESTER_NAME TEXT, NOTE TEXT, FOREIGN KEY (SEMESTER_NAME) REFERENCES $TABLE_SEMESTER ON DELETE CASCADE)")
        db.execSQL("CREATE TABLE $TABLE_COURSE (COURSE_NAME TEXT PRIMARY KEY, CLASS_NAME TEXT, START_DAY TEXT, END_DAY TEXT, NOTE TEXT, FOREIGN KEY (CLASS_NAME) REFERENCES $TABLE_CLASS ON DELETE CASCADE)")
        db.execSQL("CREATE TABLE $TABLE_SCHEDULE(ID INTEGER PRIMARY KEY AUTOINCREMENT, SEMESTER_NAME TEXT, CLASS_NAME TEXT, COURSE_NAME TEXT,FROM_HOUR TEXT, TO_HOUR TEXT, NOTE TEXT, FOREIGN KEY (SEMESTER_NAME) REFERENCES $TABLE_SEMESTER ON DELETE CASCADE,  FOREIGN KEY (CLASS_NAME) REFERENCES $TABLE_CLASS ON DELETE CASCADE, FOREIGN KEY (COURSE_NAME) REFERENCES $TABLE_COURSE ON DELETE CASCADE )")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}