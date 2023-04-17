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

import java.util.Calendar;
import java.util.List;

public class AddNewCourse extends AppCompatActivity {

    private int termID;

    TermDAO termDAO;
    CourseDAO courseDAO;
    List<Term> allTerm;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    private DatePickerDialog startDatePickerDialog,endDatePickerDialog;
    private Button startDateButton;
    private Button endDateButton;

    RadioGroup radioGroup;
    RadioButton upcomingButton, inProgressButton, completedButton;
    String selectedButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);

        initDatePicker();
        startDateButton = findViewById(R.id.courseStartDatePickerButton);
        endDateButton = findViewById(R.id.courseEndDatePickerButton);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        startDateButton.setText(getTodaysDate());
        endDateButton.setText(getTodaysDate());

        upcomingButton = (RadioButton)findViewById(R.id.radio_upcoming);
        inProgressButton = (RadioButton)findViewById(R.id.radio_in_progress);
        completedButton = (RadioButton)findViewById(R.id.radio_completed);


        //Create termDAO instance through DatabaseConn abstract class because TermDAO does not have a constructor
        termDAO = DatabaseConn.getDBInstance(getApplicationContext()).getTermDao();
        courseDAO = DatabaseConn.getDBInstance(getApplicationContext()).getCourseDao();
        //using the termDAO created above to getAllTerms
        allTerm = termDAO.getAllTerms();
        //Converting the term list to a term Array

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

                if (upcomingButton.isChecked()) {
                    selectedButton = upcomingButton.getText().toString();
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

        //        Toast.makeText(this, selectedCourse, Toast.LENGTH_SHORT).show();
        if (selectedTerm.trim().isEmpty() | selectedTerm.equals("Choose a Term") ) {
            // Show an error message to the user that a course must be selected.
            Toast.makeText(this, "Please select a Term.", Toast.LENGTH_SHORT).show();
            return;
        }

        termID = termDAO.getTermIDByTitle(selectedTerm);


        Course course = new Course(termID, courseTitle, courseInstructorName, courseInstructorEmail, courseInstructorPhone, courseStatus, courseStartDate, courseEndDate, courseNote);
//        course.courseTitle = courseTitle;
//        course.courseInstructorName = courseInstructorName;
//        course.courseInstructorEmail = courseInstructorEmail;
//        course.courseInstructorPhone = courseInstructorPhone;
//        course.courseStatus = courseStatus;
//        course.courseStartDate = courseStartDate;
//        course.courseEndDate = courseEndDate;

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

                month = month + 1;
                String date = makeDateString(day,month, year);

                startDateButton.setText(date);

//                startDateButton.setText(date);
//                endDateButton.setText(date);

            }
        };

        DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month= month + 1;
                String date = makeDateString(day, month, year);
                endDateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        startDatePickerDialog = new DatePickerDialog(this, style, startDateSetListener, year, month, day);
        endDatePickerDialog = new DatePickerDialog(this, style, endDateSetListener, year, month, day);

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

