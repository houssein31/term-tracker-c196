package com.example.termtrackerc196;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button termBtn, courseBtn, assessmentBtn;
    String username;
    int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        termBtn = findViewById(R.id.term_btn);
        courseBtn = findViewById(R.id.course_btn);
        assessmentBtn = findViewById(R.id.assessment_btn);

    }

    public void launchTerm(View v) {
        //Launch Term

        Intent i = new Intent(this, MultiPageActivity.class);
        i.putExtra("type", termBtn.getText());
        startActivity(i);
    }

    public void launchCourse(View v) {
        //Launch Course

        Intent i = new Intent(this, MultiPageActivity.class);
        i.putExtra("type", courseBtn.getText());
        startActivity(i);
    }

    public void launchAssessment(View v) {
        //Launch Assessments

        Intent i = new Intent(this, MultiPageActivity.class);
        i.putExtra("type", assessmentBtn.getText());
        startActivity(i);
    }
}