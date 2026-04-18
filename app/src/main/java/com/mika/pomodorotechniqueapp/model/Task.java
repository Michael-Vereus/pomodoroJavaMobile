package com.mika.pomodorotechniqueapp.model;

public class Task {
    private String taskName;
    private float taskMultiplier;
    private float actualPomodoros;
    private boolean isComplete;

    public Task(String taskName, float taskMultiplier){
        this.taskName = taskName;
        this.taskMultiplier = taskMultiplier;
        this.isComplete = false;
        this.actualPomodoros = 0;
    }


    public float getTaskMultiplier() {
        return taskMultiplier;
    }
    public String getTaskName() {
        return taskName;
    }
    public float getActualPomodoros() {return actualPomodoros;}
    public boolean isComplete() {return isComplete;}

    public void complete(){
        this.actualPomodoros = this.taskMultiplier;
        this.isComplete = true;
    }
    public void notComplete(){
        this.actualPomodoros = 0;
        this.isComplete = false;
    }
}
