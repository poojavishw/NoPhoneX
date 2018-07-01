package com.nophonex.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.nophonex.R
import com.nophonex.utils.Base
import com.nophonex.utils.TimerTaskSync
import com.nophonex.utils.themeUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Base(), TimerTaskSync.TimerCallback, View.OnClickListener {


    override fun onTimerListener(date: String) {
        tv_time.text = date
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeUtils.onActivityCreateSetTheme(this)
        setContentView(R.layout.activity_main)
        setListeners()
        setTimerUpdate()
    }

    private fun setListeners() {
        tv_tasks.setOnClickListener(this)
        tv_call.setOnClickListener(this)
        tv_settings.setOnClickListener(this)
        tv_feedback.setOnClickListener(this)
        tv_directions.setOnClickListener(this)
    }

    private fun setTimerUpdate() {
        val timerTask = TimerTaskSync(this, this);
        timerTask.start()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv_tasks -> startActivity(Intent(this, TasksActivity::class.java))
            R.id.tv_call -> startActivity(Intent(this, CallActivity::class.java))
            R.id.tv_settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.tv_feedback -> emailFeedback()
            R.id.tv_directions -> startActivity(Intent(this, MapsActivity::class.java))
        }
    }

    private fun emailFeedback(){
        var emailIntent = Intent(android.content.Intent.ACTION_SEND);

/* Fill it with Data */
        emailIntent.setType("plain/text")
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "vishwpooja@gmail.com")
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "NoPhoneX Feature Request")
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Your App Rocks")
//        emailIntent.setData(Uri.parse("mailto:vishwpooja@gmail.com"))

/* Send it off to the Activity-Chooser */
        startActivity(Intent.createChooser(emailIntent, "Send mail..."))
    }
}
