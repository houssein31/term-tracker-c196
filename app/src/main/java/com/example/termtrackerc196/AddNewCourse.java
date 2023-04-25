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
import android.widget.Toast;

import com.example.termtrackerc196.db.DAOs.CourseDAO;
import com.example.termtrackerc196.db.DAOs.TermDAO;
import com.example.termtrackerc196.db.DatabaseConn;
import com.example.termtrackerc196.db.Entities.Course;
import com.example.termtrackerc196.db.Entities.Term;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddNewCourse extends AppCompatActivity {

    private int termID;

    TermDAO termDAO;
    CourseDAO courseDAO;
    List<Term> allTerm;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    private DatePickerDialog startDatePickerDialog,endDatePickerDialog;
    private DatePickerDialog courseStartDatePickerDialog, courseEndDatePickerDialog;


    private Button courseStartDateButton;
    private Button courseEndDateButton;
    private Button startDateButton;
    private Button endDateButton;

    RadioGroup radioGroup;
    RadioButton droppedButton, inProgressButton, completedButton, planToTakeButton;
    String selectedButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);
        setTitle("Add New Course");

        courseStartDateButton = findViewById(R.id.courseStartDatePickerButton);
        courseEndDateButton = findViewById(R.id.courseEndDatePickerButton);


        courseStartDatePickerDialog = new DatePickerDialog(
                AddNewCourse.this,
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
                AddNewCourse.this,
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

        courseStartDateButton.setText(getTodaysDate());
        courseEndDateButton.setText(getTodaysDate());

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

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        droppedButton = (RadioButton)findViewById(R.id.radio_dropped);
        planToTakeButton = (RadioButton)findViewById(R.id.radio_plantotake);

        inProgressButton = (RadioButton)findViewById(R.id.radio_in_progress);
        completedButton = (RadioButton)findViewById(R.id.radio_completed);


        termDAO = DatabaseConn.getDBInstance(getApplicationContext()).getTermDao();
        courseDAO = DatabaseConn.getDBInstance(getApplicationContext()).getCourseDao();
        allTerm = termDAO.getAllTerms();

        String[] termTitles = new String[allTerm.size()];
        for (int i = 0; i<termTitles.length; i++) {
            termTitles[i] = allTerm.get(i).getTermTitle();
        }

        adapterItems = new ArrayAdapter<String>(this, R.layout.drop_down_item, termTitles);

        autoCompleteTextView.setAdapter(adapterItems);



        final EditText courseTitleInput = findViewById(R.id.courseTitleInput);
        final EditText courseInstructorNameInput = findViewById(R.id.courseInstructorNameInput);
        final EditText courseInstructorEmailInput = findViewById(R.id.courseInstructorEmailInput);
        final EditText courseInstructorPhoneInput = findViewById(R.id.courseInstructorPhoneInput);
        final RadioGroup courseStatusInput = findViewById(R.id.status_radiogroup);
        final Button courseStartDateInput = findViewById(R.id.courseStartDatePickerButton);
        final Button courseEndDateInput = findViewById(R.id.courseEndDatePickerButton);
        final EditText courseNoteInput = findViewById(R.id.courseNoteInput);
        Button saveButton = findViewById(R.id.saveButton);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (courseStatusInput.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select a term status", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (courseStartDateButton.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a Start Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (courseEndDateButton.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter an End Date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (droppedButton.isChecked()) {
                    selectedButton = droppedButton.getText().toString();
                } else if(planToTakeButton.isChecked()){
                    selectedButton = planToTakeButton.getText().toString();
                } else if(inProgressButton.isChecked()) {
                    selectedButton = inProgressButton.getText().toString();
                } else if(completedButton.isChecked()) {
                    selectedButton = completedButton.getText().toString();
                }

                saveNewCourse(courseTitleInput.getText().toString(), courseInstructorNameInput.getText().toString(), courseInstructorEmailInput.getText().toString(), courseInstructorPhoneInput.getText().toString(), selectedButton, courseStartDateInput.getText().toString(), courseEndDateInput.getText().toString(), courseNoteInput.getText().toString());
            }
        });

    }

    private void saveNewCourse(String courseTitle, String courseInstructorName, String courseInstructorEmail, String courseInstructorPhone, String courseStatus, String courseStartDate, String courseEndDate, String courseNote) {

        String selectedTerm = autoCompleteTextView.getText().toString();

        if (selectedTerm.trim().isEmpty() | selectedTerm.equals("Choose a Term") ) {
            Toast.makeText(this, "Please select a Term.", Toast.LENGTH_SHORT).show();
            return;
        }


        termID = termDAO.getTermIDByTitle(selectedTerm);

        Course course = new Course(termID, courseTitle, courseInstructorName, courseInstructorEmail, courseInstructorPhone, courseStatus, courseStartDate, courseEndDate, courseNote);

        courseDAO.insertCourse(course);
        launchMultiPageActivity();
        super.finish();

    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month +1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(month, day, year);

    }

    private String makeDateString(int year, int month, int day) {
        return year + "/" + month + "/" + day;
    }

    public void openCourseStartDatePicker(View view) {
        startDatePickerDialog.show();
    }

    public void openCourseEndDatePicker(View view) {
        endDatePickerDialog.show();
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

