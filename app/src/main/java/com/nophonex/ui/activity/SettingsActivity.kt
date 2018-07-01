package com.nophonex.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.nophonex.R
import com.nophonex.utils.Base
import com.nophonex.utils.TimerTaskSync
import com.nophonex.utils.themeUtils
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : Base(), View.OnClickListener, TimerTaskSync.TimerCallback {
    override fun onTimerListener(date: String?) {
        tv_time.text = date
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv_device_settings -> openSettings()
            R.id.tv_set_theme -> {
                changeTheme()
            }
        }
    }

    private fun changeTheme() {
        if (savedTheme == themeUtils.BLACK) {
            themeUtils.changeToTheme(this, themeUtils.BLUE)
            saveTheme(themeUtils.BLUE)
        } else {
            themeUtils.changeToTheme(this, themeUtils.BLACK)
            saveTheme(themeUtils.BLACK)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        themeUtils.onActivityCreateSetTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        tv_device_settings.setOnClickListener(this)
        tv_set_theme.setOnClickListener(this)
        setTimerUpdate()
    }

    private fun setTimerUpdate() {
        val timerTask = TimerTaskSync(this, this)
        timerTask.start()
    }

    private fun openSettings() {
        startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)
    }

    override fun onBackPressed() {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }
}
