package com.nophonex.utils;

import android.app.Activity;
import java.text.SimpleDateFormat;

public class TimerTaskSync extends Thread {

    private TimerCallback mTimerListener;
    private Activity mActivity;

    public TimerTaskSync(Activity activity, TimerCallback timerListener) {
        mTimerListener = timerListener;
        mActivity = activity;
    }


    public interface TimerCallback {
        void onTimerListener(String date);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        long date = System.currentTimeMillis();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, hh : mm a");
                        String currentDate = dateFormat.format(date);
                        mTimerListener.onTimerListener(currentDate);
                    }
                });
            }
        } catch (InterruptedException e) {
        }
    }


}