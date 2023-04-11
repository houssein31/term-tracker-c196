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

//        Button addNewTermButton = findViewById(R.id.addNewTermButton);
//        addNewTermButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivityForResult(new Intent(MainActivity.this ));
//            }
//        });

//        // Terms Database
//        DatabaseConn db = Room.databaseBuilder(getApplicationContext(),
//                DatabaseConn.class, "terms-database").allowMainThreadQueries().build();
//
//        Term term1 = new Term("term1", LocalDate.of(2023, 1, 15), LocalDate.of(2023, 2, 20));
//        Term term2 = new Term("term2", LocalDate.of(2023, 1, 15), LocalDate.of(2023, 2, 20));
//
//        db.termsDao().insertAll(term1, term2);
//
//        List<Term> termList = db.termsDao().getAllTerms();



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