package com.mika.pomodorotechniqueapp.utility;

public interface OnTimerEventListener {
    void onTick(Long millisLeft);
    void onFinish();
}
