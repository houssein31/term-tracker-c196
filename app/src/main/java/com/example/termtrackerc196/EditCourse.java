package com.example.termtrackerc196;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.termtrackerc196.db.DAOs.CourseDAO;
import com.example.termtrackerc196.db.DAOs.TermDAO;
import com.example.termtrackerc196.db.DatabaseConn;
import com.example.termtrackerc196.db.Entities.Course;
import com.example.termtrackerc196.db.Entities.Term;

import java.util.Calendar;
import java.util.List;

public class EditCourse extends AppCompatActivity {

    private EditText courseTitleInput;
    private EditText courseInstructorNameInput;
    private EditText courseInstructorEmailInput;
    private EditText courseInstructorPhoneInput;

    private RadioGroup courseStatusInput;
    private EditText courseNoteInput;
    private int termID;
    TermDAO termDAO;

    List<Term> allTerm;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    private DatePickerDialog courseStartDatePickerDialog, courseEndDatePickerDialog;
    private Button courseStartDateButton;
    private Button courseEndDateButton;



    private int courseID;
    private CourseDAO courseDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        setTitle("Edit Course");

        courseTitleInput = findViewById(R.id.courseTitleInput);
        courseInstructorNameInput = findViewById(R.id.courseInstructorNameInput);
        courseInstructorEmailInput = findViewById(R.id.courseInstructorEmailInput);
        courseInstructorPhoneInput = findViewById(R.id.courseInstructorPhoneInput);
        courseStatusInput = findViewById(R.id.status_course_radiogroup);
        courseStartDateButton = findViewById(R.id.courseStartDatePickerButton);
        courseEndDateButton = findViewById(R.id.courseEndDatePickerButton);
        courseNoteInput = findViewById(R.id.courseNoteInput);

        termDAO = DatabaseConn.getDBInstance(getApplicationContext()).getTermDao();
        courseDAO = DatabaseConn.getDBInstance(getApplicationContext()).getCourseDao();

        courseID = getIntent().getIntExtra("courseID", -1);
        Course course = courseDAO.getCourseByID(courseID);


        courseTitleInput.setText(course.getCourseTitle());
        courseInstructorNameInput.setText(course.getCourseInstructorName());
        courseInstructorEmailInput.setText(course.getCourseInstructorEmail());
        courseInstructorPhoneInput.setText(course.getCourseInstructorPhone());

        if (course.getCourseStatus().equals("Dropped")) {
            courseStatusInput.check(R.id.radio_course_dropped);
        } else if (course.getCourseStatus().equals("In Progress")) {
            courseStatusInput.check(R.id.radio_course_in_progress);
        } else if (course.getCourseStatus().equals("Plan to Take")) {
            courseStatusInput.check(R.id.radio_course_plantotake);
        } else {
            courseStatusInput.check(R.id.radio_course_completed);
        }

        courseStartDatePickerDialog = new DatePickerDialog(
                EditCourse.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        courseStartDateButton.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        courseEndDatePickerDialog = new DatePickerDialog(
                EditCourse.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        courseEndDateButton.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        courseStartDateButton.setText(course.getCourseStartDate());
        courseEndDateButton.setText(course.getCourseEndDate());

        courseStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseStartDatePickerDialog.show();
            }
        });

        courseEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseEndDatePickerDialog.show();
            }
        });

        courseNoteInput.setText(course.getCourseNote());

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        allTerm = termDAO.getAllTerms();

        String[] termTitles = new String[allTerm.size()];
        for (int i = 0; i < termTitles.length; i++) {
            termTitles[i] = allTerm.get(i).getTermTitle();
        }

        int selectedIndex = -1;
        for (int i = 0; i < allTerm.size(); i++) {
            if (allTerm.get(i).getTermId() == course.getTermID()) {
                selectedIndex = i;
                break;
            }
        }

        autoCompleteTextView.setText(termTitles[selectedIndex]);
        adapterItems = new ArrayAdapter<String>(this, R.layout.drop_down_item, termTitles);
        autoCompleteTextView.setAdapter(adapterItems);


        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String courseTitle = courseTitleInput.getText().toString();
                String courseInstructorName = courseInstructorNameInput.getText().toString();
                String courseInstructorEmail = courseInstructorEmailInput.getText().toString();
                String courseInstructorPhone = courseInstructorPhoneInput.getText().toString();

                int selectedStatusID = courseStatusInput.getCheckedRadioButtonId();
                String courseStatus = ((RadioButton) findViewById(selectedStatusID)).getText().toString();

                String courseStartDate = courseStartDateButton.getText().toString();

                String courseEndDate = courseEndDateButton.getText().toString();

                String courseNote = courseNoteInput.getText().toString();

                String selectedTerm = autoCompleteTextView.getText().toString();
                int termID = termDAO.getTermIDByTitle(selectedTerm);


                courseDAO.updateCourseByID(courseID, courseTitle, courseInstructorName, courseInstructorEmail, courseInstructorPhone, courseStatus, courseStartDate, courseEndDate, courseNote, termID);

                Intent intent = new Intent(EditCourse.this, DisplayCourse.class);
                intent.putExtra("courseID", courseID);
                startActivity(intent);
                EditCourse.super.finish();
            }
        });
    }


    public void openCourseStartDatePicker(View view) {
        courseStartDatePickerDialog.show();
    }
    public void openCourseEndDatePicker(View view) {
        courseEndDatePickerDialog.show();
    }

    private void launchMultiPageActivity() {
        Intent i = new Intent(this, MultiPageActivity.class);
        i.putExtra("type", "Courses");
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
