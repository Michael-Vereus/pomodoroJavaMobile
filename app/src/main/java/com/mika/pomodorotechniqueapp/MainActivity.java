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
import com.mika.pomodorotechniqueapp.model.SessionMode;
import com.mika.pomodorotechniqueapp.model.Task;
import com.mika.pomodorotechniqueapp.utility.OnTimerEventListener;
import com.mika.pomodorotechniqueapp.utility.TaskAdapter;
import com.mika.pomodorotechniqueapp.utility.TimerManager;

import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {
    private Button pomodoro, shortBreak, longBreak, startOrPause, addTask;
    private TextView countdown, currentTask, footer;
    private ListView taskList;
    private TaskController taskController;
    private TimerManager timerManager;
    private SessionMode currentMode = SessionMode.POMODORO;
    // removed timeLeftMillis, isRunning, and CountDownTimer and moved it all to a stand alone class (TimerManager)

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

        this.timerManager = new TimerManager(currentMode.toMillis(), new OnTimerEventListener() {
            @Override
            public void onTick(Long millisLeft) {
                updateTimerDisplay(millisLeft);
            }

            @Override
            public void onFinish() {
                startOrPause.setText("Start");
            }
        });

        TaskAdapter taskAdapter = new TaskAdapter(
            MainActivity.this,
            taskController.getListOfTask(),
            nextTask -> {
                if (nextTask == null){
                    timerManager.reset(0);
                    countdown.setText("Done");
                    currentTask.setText("All tasks completed !");
                    setFooter();
                    return;
                }
                // nti tinggal ambil waktu dri session mode e
                long newDuration = currentMode.getTaskTime(nextTask);
                timerManager.reset(newDuration);
                startOrPause.setText("Start");

                // update the display
                currentTask.setText(nextTask.getTaskName());
                updateTimerDisplay(timerManager.getTimeLeftMillis());
                setFooter();
        });
        taskList.setAdapter(taskAdapter);

        // ini buat pas pertama kali start
        Task first = taskController.nextTask();
        if (first != null) {
            currentTask.setText(first.getTaskName());
            timerManager.reset(currentMode.getTaskTime(first));
        }
        updateTimerDisplay(timerManager.getTimeLeftMillis());
        setFooter();

        // startOrPause listener buat start / stop timer
        startOrPause.setOnClickListener(v -> {
            if (timerManager.isRunning()) {
                timerManager.pauseTimer();
                startOrPause.setText("Start");
            } else {
                timerManager.startTimer();
                startOrPause.setText("Pause");
            }
        });

        this.pomodoro.setOnClickListener(v -> switchMode(SessionMode.POMODORO));
        this.shortBreak.setOnClickListener(v -> switchMode(SessionMode.SHORT_BREAK));
        this.longBreak.setOnClickListener(v -> switchMode(SessionMode.LONG_BREAK));

        addTask.setOnClickListener(v -> { // i got inspired from the concept of modal in bootstrap
            // basically inflate the layout manually
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
                            taskAdapter.notifyDataSetChanged();
                            Task nextTask = taskController.nextTask();
                            if (nextTask != null && currentMode == SessionMode.POMODORO){
                                timerManager.reset(currentMode.getTaskTime(nextTask));
                                currentTask.setText(nextTask.getTaskName());
                                updateTimerDisplay(timerManager.getTimeLeftMillis());
                            }
                            setFooter();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void switchMode(SessionMode mode) {
        currentMode = mode;

        // calculate new duration — for POMODORO, scale by active task if one exists
        Task activeTask = taskController.nextTask();
        long newDuration = currentMode.getTaskTime(activeTask);

        timerManager.reset(newDuration);
        startOrPause.setText("Start");
        updateTimerDisplay(timerManager.getTimeLeftMillis());
        setFooter();
    }
    // update time display tiap detik
    private void updateTimerDisplay(long timeLeftMillis) {
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