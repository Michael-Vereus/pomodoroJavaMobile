package com.mika.pomodorotechniqueapp.model;

public enum SessionMode {
    POMODORO(25),
    SHORT_BREAK(5),
    LONG_BREAK(15);

    private final int minutes;
    SessionMode(int minutes) {
        this.minutes = minutes;
    }
    public long toMillis() {
        return (long) minutes * 60 * 1000;
    }
    public long getTaskTime(Task task){
        if (this == POMODORO && task!= null){
            return (long) (task.getTaskMultiplier() * toMillis());
        }
        return toMillis();
    }
    public String getLabel(){
        switch (this) {
            case POMODORO: return "POMODORO";
            case SHORT_BREAK: return "SHORT_BREAK";
            case LONG_BREAK: return "LONG_BREAK";
            default: return "";
        }
    }
}
