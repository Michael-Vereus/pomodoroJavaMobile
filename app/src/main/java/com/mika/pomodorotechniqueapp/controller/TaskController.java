package com.mika.pomodorotechniqueapp.controller;

import com.mika.pomodorotechniqueapp.model.Task;
import com.mika.pomodorotechniqueapp.repository.TaskRepository;

import java.util.List;

public class TaskController {
    private final TaskRepository taskRepository;

    public TaskController(){
        this.taskRepository = new TaskRepository();
    }

    public List<Task> getListOfTask(){
        return taskRepository.getListOfTask();
    }
    public void addTask(String taskName, float taskPomodoroMultiplier){
        taskRepository.addTask(new Task(taskName, taskPomodoroMultiplier));
    }
    public float getCompletedPomodoroMultiplier(){
        float totalMultiplier = 0;
        for(Task t : getListOfTask()){
            if (t.isComplete()){
                totalMultiplier = t.getTaskMultiplier() + totalMultiplier;
            }
        }
        return totalMultiplier;
    }
    public float getTotalPomodoroMultiplier(){
        float totalMultiplier = 0;
        for(Task t : getListOfTask()){
            totalMultiplier = t.getTaskMultiplier() + totalMultiplier;
        }
        return totalMultiplier;
    }
    public Task nextTask(){
        for(Task t : getListOfTask()){
            if (!t.isComplete()){
                return t;
            }
        }
        return null;
    }
}
