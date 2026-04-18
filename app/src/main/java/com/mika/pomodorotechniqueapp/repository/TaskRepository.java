package com.mika.pomodorotechniqueapp.repository;

import com.mika.pomodorotechniqueapp.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private List<Task> listOfTask;
    public TaskRepository(){
        this.listOfTask = new ArrayList<>();
        listOfTask.add(new Task("Code", 1));
        listOfTask.add(new Task("Rally Time", 2));
        listOfTask.add(new Task("Sleep", 3));
    }

    public List<Task> getListOfTask() {return listOfTask;}

    public void addTask(Task newTask){
        this.listOfTask.add(newTask);
    }
}
