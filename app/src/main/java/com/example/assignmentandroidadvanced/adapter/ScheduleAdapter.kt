package com.example.assignmentandroidadvanced.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentandroidadvanced.R
import com.example.assignmentandroidadvanced.database.Database
import com.example.assignmentandroidadvanced.database.ScheduleDB
import com.example.assignmentandroidadvanced.model.Schedule

class ScheduleAdapter(
    private val mContext: Context,
    private var scheduleList: ArrayList<Schedule>
) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val order: TextView = itemView.findViewById(R.id.txtScheduleOrder)
        val semesterName: TextView = itemView.findViewById(R.id.txtScheduleSemesterName)
        val className: TextView = itemView.findViewById(R.id.txtScheduleClassName)
        val startTime: TextView = itemView.findViewById(R.id.txtScheduleStartTime)
        val endTime: TextView = itemView.findViewById(R.id.txtScheduleEndTime)
        val note: TextView = itemView.findViewById(R.id.txtScheduleNote)
        val scheduleItem:RelativeLayout = itemView.findViewById(R.id.relativeLayoutScheduleItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.schedule_item, null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.order.text = "Thứ tự: ${scheduleList[position].Id.toString()}"
        holder.semesterName.text = "Kỳ học: ${scheduleList[position].semesterName}"
        holder.className.text = "Lớp: ${ scheduleList[position].className}"
        holder.startTime.text = scheduleList[position].fromHour
        holder.endTime.text = scheduleList[position].toHour
        holder.note.text = scheduleList[position].note

        holder.scheduleItem.setOnLongClickListener {
            val scheduleDB = ScheduleDB(Database(mContext))
            AlertDialog.Builder(mContext).apply {
                setTitle(mContext.getText(R.string.confirm_delete))
                setNegativeButton(mContext.getText(R.string.cancel)) {dialog, _ ->
                    dialog.dismiss()
                }
                setPositiveButton(mContext.getText(R.string.delete)) {_, _ ->
                    if(scheduleDB.removeSchedule(scheduleList[position].Id)) {
                        Toast.makeText(mContext, mContext.getText(R.string.delete_successfully), Toast.LENGTH_SHORT).show()
                        this@ScheduleAdapter.notifyItemRemoved(position)
                        scheduleList.clear()
                        scheduleList = scheduleDB.getAllSchedule()
                        this@ScheduleAdapter.notifyItemRangeChanged(position, scheduleList.size)
                    } else{
                        Toast.makeText(mContext, mContext.getText(R.string.delete_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            }.create().show()
            true
        }
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }
}