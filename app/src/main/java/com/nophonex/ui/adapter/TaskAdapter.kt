package com.nophonex.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nophonex.R
import com.nophonex.ui.adapter.TaskAdapter.TasksHolder
import kotlinx.android.synthetic.main.tasks_list_item.view.*
import java.util.ArrayList

class TaskAdapter(private val mContext: Context) : RecyclerView.Adapter<TasksHolder>() {
    private val mLayoutInflater: LayoutInflater
    private var mListTodoTasks: List<String>? = null

    init {
        mLayoutInflater = LayoutInflater.from(mContext)
        mListTodoTasks = ArrayList<String>()
    }

    override fun getItemCount(): Int {
        return mListTodoTasks?.size!!
    }

    override fun onBindViewHolder(holder: TasksHolder, position: Int) {
        holder.enteredToDo?.text = mListTodoTasks?.get(position)
        holder.rlToDoItem.setOnClickListener(View.OnClickListener {
            if(!holder.todoCheck.isChecked) {
                holder.todoCheck.isChecked = true
                holder.enteredToDo.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            }else{
                holder.enteredToDo.setPaintFlags( holder.enteredToDo.getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                holder.todoCheck.isChecked = false
            }
        })
        holder.todoCheck.setOnClickListener(View.OnClickListener {
           setStrikethrough(holder)
        })
    }

    private fun setStrikethrough(holder: TasksHolder) {
        if(!holder.todoCheck.isChecked) {
            holder.todoCheck.isChecked = false
            holder.enteredToDo.setPaintFlags( holder.enteredToDo.getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv())
        }else{
            holder.enteredToDo.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            holder.todoCheck.isChecked = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksHolder {
        val view = mLayoutInflater.inflate(R.layout.tasks_list_item, parent, false)
        return TasksHolder(view)
    }

    class TasksHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val todoCheck = view.checkBox
        val enteredToDo = view.tv_entered_todo
        val rlToDoItem = view.rl_todo_item
    }

    fun setTodoTaskList(todoTaskList: List<String>) {
        mListTodoTasks = todoTaskList
        notifyDataSetChanged()
    }
}