package com.mika.pomodorotechniqueapp.repository;

import com.mika.pomodorotechniqueapp.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private List<Task> listOfTask;
    public TaskRepository(){
        this.listOfTask = new ArrayList<>();
    }

    public List<Task> getListOfTask() {return listOfTask;}

    public void addTask(Task newTask){
        this.listOfTask.add(newTask);
    }
}
