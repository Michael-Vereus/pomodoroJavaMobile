package com.mika.pomodorotechniqueapp.utility;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mika.pomodorotechniqueapp.R;
import com.mika.pomodorotechniqueapp.model.Task;

import java.util.List;

public class TaskAdapter extends BaseAdapter {

    private Context context;
    private List<Task> taskList;
    LayoutInflater layoutInflater;
    private OnTaskActionListener listener;

    public TaskAdapter(Context context, List<Task> taskList, OnTaskActionListener actionListener){
        this.context = context;
        this.taskList = taskList;
        this.layoutInflater = (LayoutInflater.from(context));
        this.listener = actionListener;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_task_view, parent, false);
        }
        TextView taskName = (TextView) convertView.findViewById(R.id.taskName);
        TextView taskMultiplier = (TextView) convertView.findViewById(R.id.taskTimeMultiplier);
        Button checkBtn         = convertView.findViewById(R.id.checkBtn);

        Task task = taskList.get(position);
        taskName.setText(task.getTaskName());
        // jdi ini check en sek status task e ws sls ta belum
        if (task.isComplete()) {
            taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            taskName.setPaintFlags(taskName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        // display value pomodor di samping e nama task
        taskMultiplier.setText(task.getActualPomodoros() + "/" + task.getTaskMultiplier());

        // ini listener but tiap tombol centang di masing" task
        checkBtn.setOnClickListener(v -> {
            if (task.isComplete() == true){task.notComplete();}
            else {task.complete();}

            notifyDataSetChanged(); // update dataset, basically re summon getview
            listener.onTaskCompleted(findNextTask()); // ini buat call back ke main act
        });
        return convertView;
    }
    private Task findNextTask() {
        for (Task t : taskList) {
            if (!t.isComplete()) return t;
        }
        return null; // all tasks done
    }
}
