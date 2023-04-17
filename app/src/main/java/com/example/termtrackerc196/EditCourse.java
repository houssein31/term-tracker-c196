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

    RadioGroup radioGroup;
    RadioButton upcomingButton, inProgressButton, completedButton;
    String selectedTypeButton, selectedStatusButton;
    TextView textView;

    private int courseID;
    private CourseDAO courseDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

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


        // Set the appropriate radio button based on the assessment
        // status
        if (course.getCourseStatus().equals("Upcoming")) {
            courseStatusInput.check(R.id.radio_course_upcoming);
        } else if (course.getCourseStatus().equals("In Progress")) {
            courseStatusInput.check(R.id.radio_course_in_progress);
        } else {
            courseStatusInput.check(R.id.radio_course_completed);
        }

        courseStartDatePickerDialog = new DatePickerDialog(
                EditCourse.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the end date text when the user chooses a date
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
                        // Set the end date text when the user chooses a date
                        courseEndDateButton.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        // Set the end date text to the assessment's current end date
        courseStartDateButton.setText(course.getCourseStartDate());
        courseEndDateButton.setText(course.getCourseEndDate());

        // Set the onClickListener for the end date button
        courseStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the date picker dialog when the user clicks the button
                courseStartDatePickerDialog.show();
            }
        });

        // Set the onClickListener for the end date button
        courseEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the date picker dialog when the user clicks the button
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

        // Get the index of the course with the ID of the assessment's courseID
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

    private void editCourse() {
        Intent i = new Intent(this, EditCourse.class);
        startActivity(i);
        super.finish();
    }

    private void saveNewCourse(String courseTitle, String courseInstructorName, String courseInstructorEmail, String courseInstructorPhone, String courseStatus, String copurseStartDate, String courseEndDate, String courseNote) {

        String selectedCourse = autoCompleteTextView.getText().toString();

        termID = termDAO.getTermIDByTitle(selectedCourse);


        Course course = new Course(termID, courseTitle, courseInstructorName, courseInstructorEmail, courseInstructorPhone, courseStatus, copurseStartDate, courseEndDate, courseNote);


        courseDAO.insertCourse(course);
//        finish();
        launchMultiPageActivity();
        super.finish();
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month +1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);

    }

    private void initDatePicker() {

        DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month= month + 1;
                String date = makeDateString(day, month, year);
                courseStartDateButton.setText(date);
            }
        };

        DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month= month + 1;
                String date = makeDateString(day, month, year);
                courseEndDateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        courseStartDatePickerDialog = new DatePickerDialog(this, style, startDateSetListener, year, month, day);
        courseEndDatePickerDialog = new DatePickerDialog(this, style, endDateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {

        return getMonthFormat(month)  + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {

        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return  "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";
        return "JAN";
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
