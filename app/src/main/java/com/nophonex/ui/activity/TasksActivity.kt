package com.nophonex.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import com.nophonex.R
import com.nophonex.ui.adapter.TaskAdapter
import com.nophonex.utils.Base
import com.nophonex.utils.TimerTaskSync
import com.nophonex.utils.themeUtils
import kotlinx.android.synthetic.main.activity_tasks.*


class TasksActivity : Base(), TextView.OnEditorActionListener, TimerTaskSync.TimerCallback {
    override fun onTimerListener(date: String?) {
        tv_time.text = date
    }

    private lateinit var mTodoTasksList: MutableList<String>
    private lateinit var mRclTodoTasks: RecyclerView

    override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            hideKeyboard()
            mTodoTasksList.add(et_todo_task.text.toString())
            todoTasksList(mTodoTasksList)
            et_todo_task.setText("")
            return true
        }
        return false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeUtils.onActivityCreateSetTheme(this)
        setContentView(R.layout.activity_tasks)
        et_todo_task.setOnEditorActionListener(this)
        mTodoTasksList = mutableListOf()
        getSharedPrefTaskList()
        setAdapter()
        setTimerUpdate()
    }

    private fun getSharedPrefTaskList() {
        if(null != getTodoTasksList() && getTodoTasksList()?.size!! > 0){
            mTodoTasksList = getTodoTasksList() as MutableList<String>
        }
    }

    private fun setAdapter() {
        mRclTodoTasks = findViewById<RecyclerView>(R.id.rcl_tasks);
        mRclTodoTasks.layoutManager =  LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val taskAdapter =TaskAdapter(this)
        mRclTodoTasks.adapter = taskAdapter
        taskAdapter.setTodoTaskList(mTodoTasksList)
    }

    private fun setTimerUpdate() {
        val timerTask = TimerTaskSync(this, this);
        timerTask.start()
    }
}