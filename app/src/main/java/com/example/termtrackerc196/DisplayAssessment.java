package com.example.termtrackerc196;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.termtrackerc196.db.DAOs.AssessmentDAO;
import com.example.termtrackerc196.db.DAOs.CourseDAO;
import com.example.termtrackerc196.db.DAOs.TermDAO;
import com.example.termtrackerc196.db.DatabaseConn;
import com.example.termtrackerc196.db.Entities.Assessment;
import com.example.termtrackerc196.db.Entities.Course;

import java.util.List;

public class DisplayAssessment extends AppCompatActivity {

    String type;
    Button addNewItemButton;
    int assessmentID;
    CourseDAO courseDAO;
    AssessmentDAO assessmentDAO;
    List<Assessment> assessmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_assessment);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            assessmentID = extras.getInt("assessmentID");
        }

        courseDAO = DatabaseConn.getDBInstance(getApplicationContext()).getCourseDao();
        assessmentDAO = DatabaseConn.getDBInstance(getApplicationContext()).getAssessmentDao();

        Assessment assessment = assessmentDAO.getAssessmentByID(assessmentID);

        String courseTitle = courseDAO.getCourseTitleByCourseID(assessment.getCourseID());
        String courseInstructorName = courseDAO.getCourseInstructorNameByCourseID(assessment.getCourseID());
        String assessmentTitle = assessment.getAssessmentTitle();
        String assessmentType = assessment.getAssessmentType();
        String assessmentStatus = assessment.getAssessmentStatus();
        String assessmentEndDate = assessment.getAssessmentEndDate();
        String assessmentNote = assessment.getAssessmentNote();

        TextView courseTitleTextView = findViewById(R.id.courseTitleTextView);
        TextView courseInstructorNameTextView = findViewById(R.id.courseInstructorNameTextView);
        TextView assessmentTitleTextView = findViewById(R.id.assessmentTitleTextView);
        TextView assessmentTypeTextView = findViewById(R.id.assessmentTypeTextView);
        TextView assessmentStatusTextView = findViewById(R.id.assessmentStatusTextView);
        TextView assessmentEndDateTextView = findViewById(R.id.assessmentEndDateTextView);
        TextView assessmentNoteTextView = findViewById(R.id.assessmentNoteTextView);

        courseTitleTextView.setText(courseTitle);
        courseInstructorNameTextView.setText(courseInstructorName);
        assessmentTitleTextView.setText(assessmentTitle);
        assessmentTypeTextView.setText(assessmentType);
        assessmentStatusTextView.setText(assessmentStatus);
        assessmentEndDateTextView.setText(assessmentEndDate);
        assessmentNoteTextView.setText(assessmentNote);

        setTitle(assessmentTitle);
//
//        addNewItemButton = findViewById(R.id.add_new_item_button);
//        addNewItemButton.setText("Add new " + type);

        Button editAssessmentButton = findViewById(R.id.edit_assessment_button);
        editAssessmentButton.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayAssessment.this, EditAssessment.class);
            intent.putExtra("assessmentID", assessmentID);
            intent.putExtra("assessmentTitle", assessmentTitle);
            intent.putExtra("assessmentType", assessmentType);
            intent.putExtra("assessmentStatus", assessmentStatus);
            intent.putExtra("assessmentEndDate", assessmentEndDate);
            intent.putExtra("assessmentNote", assessmentNote);
            startActivity(intent);
        });

    }

    private void launchMultiPageActivity() {
        Intent i = new Intent(this, MultiPageActivity.class);
        i.putExtra("type", "Assessments");
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
        launchMultiPageActivity();
        super.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

}