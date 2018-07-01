package com.nophonex.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import java.util.*

open class BaseActivity: AppCompatActivity() {

    private var mPref: SharedPreferences? = null

    init {
//        mPref = this.getSharedPreferences(Constants.SHARED_PREF_FILE, Context.MODE_PRIVATE)
    }
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        mPref = getSharedPreferences(Constants.SHARED_PREF_FILE, Context.MODE_PRIVATE)
    }

    public fun getPref(): SharedPreferences{
        if(null!= mPref) {
            return mPref as SharedPreferences
        }else{
            mPref = getSharedPreferences(Constants.SHARED_PREF_FILE, Context.MODE_PRIVATE)
            return mPref as SharedPreferences
        }
    }
    fun clearPref(){
        mPref?.edit()?.clear()?.apply()
    }
    fun todoTasksList(tasklist: List<String>){
        val gson = Gson()
        val json = gson.toJson(tasklist)
        getPref().edit().putString("list",json).apply()
    }

    fun getTodoTasksList(): List<String>? {
        val gson = Gson()
        var taskList: List<String>? = null
        val list = getPref().getString("list", "")
        val json = gson.fromJson<Array<String>>(list, Array<String>::class.java)
        if (json != null) {
            taskList = Arrays.asList<String>(*json)
        }
        return taskList
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun saveTheme(theme: Int) {
        getPref().edit().putInt(Constants.THEME, theme).apply()
    }

    fun getSavedTheme(): Int {
        return getPref().getInt(Constants.THEME, 1)
    }
}