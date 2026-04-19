package com.mika.pomodorotechniqueapp.utility;

import android.os.CountDownTimer;

public class TimerManager {
    private CountDownTimer countDownTimer;
    private long timeLeftMillis;
    private boolean isRunning = false;
    private final OnTimerEventListener onTimerEventListener;

    public TimerManager(long initialMillis, OnTimerEventListener listener){
        this.timeLeftMillis = initialMillis;
        this.onTimerEventListener = listener;
    }
    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                onTimerEventListener.onTick(timeLeftMillis);
            }
            @Override
            public void onFinish() {
                isRunning = false;
                onTimerEventListener.onFinish();
            }
        }.start();

        isRunning = true;
    }
    public void pauseTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        isRunning = false;
    }
    public void reset(long newMillis) {
        pauseTimer();
        timeLeftMillis = newMillis;
    }

    public boolean isRunning()          { return isRunning; }
    public long getTimeLeftMillis()     { return timeLeftMillis; }
}
