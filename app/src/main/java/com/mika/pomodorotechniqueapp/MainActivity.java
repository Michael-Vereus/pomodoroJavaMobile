package com.mika.pomodorotechniqueapp;

import static java.time.LocalTime.now;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mika.pomodorotechniqueapp.controller.TaskController;
import com.mika.pomodorotechniqueapp.model.Task;
import com.mika.pomodorotechniqueapp.utility.TaskAdapter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {
    private Button pomodoro, shortBreak, longBreak, startOrPause, addTask;
    private TextView countdown, currentTask, footer;
    private ListView taskList;
    private TaskController taskController;


    private CountDownTimer countDownTimer;
    private long timeLeftMillis = 25 * 60 * 1000;
    // logic e di dalam semenit itu ada 60 detik sedangkan didalam 1 detik itu ada 1000 milisecond
    // jdi buat konversi dri 25 menit ke bentuk milisecond tinggal kali 60 kali 1000
    private boolean isRunning = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            int padding = 25;
            v.setPadding(systemBars.left + padding, systemBars.top + padding, systemBars.right + padding, systemBars.bottom + padding);
            return insets;
        });
        // refrencing obj to view :o, seems like a fockn boilerplate -,-
        this.pomodoro = findViewById(R.id.pomodoroBtn);
        this.countdown = findViewById(R.id.countdown);
        this.currentTask = findViewById(R.id.currentTask);
        this.shortBreak = findViewById(R.id.shortBreakBtn);
        this.longBreak = findViewById(R.id.longBreakBtn);
        this.startOrPause = findViewById(R.id.startOrPauseCountdown);
        this.addTask = findViewById(R.id.addTask);
        this.taskList = findViewById(R.id.listOfTask);
        this.footer = findViewById(R.id.footer);
        this.taskController = new TaskController();

        TaskAdapter taskAdapter = new TaskAdapter(MainActivity.this, taskController.getListOfTask(), nextTask -> {
            if (countDownTimer != null) countDownTimer.cancel(); // stop current timer

            if (nextTask == null) {
                countdown.setText("Done!");
                currentTask.setText("All tasks completed");
                timeLeftMillis = 0;
                setFooter();
                return;
            }

            // ini buat ngitung brp milisec task selanjut e
            timeLeftMillis = (long)(nextTask.getTaskMultiplier() * 25 * 60 * 1000);
            setFooter();

            // update the display
            currentTask.setText(nextTask.getTaskName());
            updateTimerDisplay();
        });
        taskList.setAdapter(taskAdapter);

        // startOrPause listener buat start / stop timer
        startOrPause.setOnClickListener(v -> {
            if (isRunning) {
                pauseTimer();
                startOrPause.setText("Start");
            } else {
                startTimer();
                startOrPause.setText("Pause");
            }
        });

        addTask.setOnClickListener(v -> {
            // inflate your custom layout — same idea as Bootstrap injecting HTML into modal body
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_task, null);

            // ambil id widget di dialog e
            EditText etName = dialogView.findViewById(R.id.task_name);
            EditText etEst  = dialogView.findViewById(R.id.defaultTime);

            new AlertDialog.Builder(this)
                    .setTitle("Add Task")
                    .setView(dialogView) // inject your layout as the modal body
                    .setPositiveButton("Add", (dialog, which) -> {
                        String name = etName.getText().toString().trim();
                        String multiplierStr = etEst.getText().toString().trim();

                        if (!name.isEmpty() && !multiplierStr.isEmpty()) {
                            float pomodoroMultiplier = Float.parseFloat(multiplierStr);
                            taskController.addTask(name, pomodoroMultiplier);
                            Task nextTask = taskController.nextTask();
                            taskAdapter.notifyDataSetChanged();
                            timeLeftMillis = (long)(nextTask.getTaskMultiplier() * 25 * 60 * 1000);
                            updateTimerDisplay();
                            setFooter();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });



    }


    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                updateTimerDisplay();
            }
            @Override
            public void onFinish() {
                startOrPause.setText("start");
                isRunning = false;
            }
        }.start();

        isRunning = true;
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isRunning = false;
    }

    // update time display tiap detik
    private void updateTimerDisplay() {
        long minutes = timeLeftMillis / 1000 / 60;
        long seconds = (timeLeftMillis / 1000) % 60;
        countdown.setText(String.format("%02d:%02d", minutes, seconds));
    }
    @SuppressLint("NewApi")
    private void setFooter(){
        float currentPomodoro = taskController.getCompletedPomodoroMultiplier();
        float totalPomodoro = taskController.getTotalPomodoroMultiplier();
        float notCompletedPodomoro = totalPomodoro - currentPomodoro;
        float minuteFloat =  25 * notCompletedPodomoro;

        LocalTime time = LocalTime.now();
        long minute  = Math.round(minuteFloat);
        LocalTime estFinish = time.plusMinutes(minute);
        // ol trick XD
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.footer.setText("Pomos " + currentPomodoro + "/" + totalPomodoro + " Finish at " + estFinish.format(formatter) + " (" + minuteFloat/60 + ")" );
    }
}