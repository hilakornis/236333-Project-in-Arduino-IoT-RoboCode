package com.example.a236333_hw3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.view.View;
import java.util.Vector;
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    LinearLayout ButtonsLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        ButtonsLayout = findViewById(R.id.ButtonsLayout);

        setTitle("Tasks");

        for (roboCodeTask task : getTasks()) {
            final roboCodeTaskButton btn = new roboCodeTaskButton(this);
            btn.RCTask = task;
            btn.setText(task.Title);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 300
            );
            params.setMargins(10, 10, 10, 0);
            btn.setLayoutParams(params);
            ButtonsLayout.addView(btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    roboCodeTask.current = ((roboCodeTaskButton)v).RCTask;
                    startActivity(new Intent(TasksActivity.this, roboCodeTaskActivity.class));
                }
            });
        }
    }

    private List<roboCodeTask> getTasks() {
        List<roboCodeTask> lst = new Vector<roboCodeTask>();
        roboCodeTask task;

        task = new roboCodeTask();
        task.ID             = 1;
        task.Points         = 2;
        task.Title          = "Do the Robot Dance";
        task.Description    = "It's the annual forklift machines dancing competition, and roboCode wants to show off his own unique dance. Could you make roboCode do his own special dance? RoboCode must drive 5 tiles forward, 3 turns in its place and then turn around and drive 5 tiles back to its first position?";
        task.Hints          = "You only have two 'half turn' cards you can use - think about how your gonna make roboCode make three full turns.";
        task.Arrangement    = "Full arrangement fot this task will only be given after a solution is uploaded. for now, assume the boxes will be arranged in a long line.";
        task.Accomplished   = false;
        lst.add(task);

        task = new roboCodeTask();
        task.ID             = 2;
        task.Points         = 10;
        task.Title          = "Track the Package";
        task.Description    = "Robocode's warehouse is a complete mess! Robocode must find package number 7 and bring it to the end point (white tile). Can you manage to get Robocode to bring package # 7?";
        task.Hints          = "The package might be anywhere, and there might also be other packages, that does not have the number seven on them. think about what you should make roboCode search any package, and then what do to in case the package is package #7 or in case it is not.";
        task.Arrangement    = "Arrangement fot this task will only be given after a solution is uploaded.";
        task.Accomplished   = false;
        lst.add(task);

        task = new roboCodeTask();
        task.ID             = 3;
        task.Points         = 20;
        task.Title          = "Sort It Up!";
        task.Description    = "A new delivery of packages has just arrived, all marked with numbers. RoboCode wants to impress its manager by sorting them in a line, arranged from the smallest number to the biggest. could you help him do that?";
        task.Hints          = "How can you compare two packages? three? Four? how many pairs of packages should be compared in order to sort any given numbers of packages, and what should be done after each compression?";
        task.Arrangement    = "Full arrangement fot this task will only be given after a solution is uploaded. for now, assume the boxes will be arranged in a long line.";
        task.Accomplished   = false;
        lst.add(task);

        task = new roboCodeTask();
        task.ID             = 4;
        task.Points         = 30;
        task.Title          = "Exit a Maze";
        task.Description    = "My oh my! It seems that RoboCode is stuck inside a maze and cannot find its way out. Can you arrange that card in such way that roboCode will find its way out? notice - roboCode can only drive  through red tiles!";
        task.Hints          = "think about what roboCode should do each time he runs into a non red tile. Could there be a permanent action he can always select to do when he runs into such tile?";
        task.Arrangement    = "Arrangement fot this task will only be given after a solution is uploaded.";
        task.Accomplished   = false;
        lst.add(task);

        return lst;
    }
}