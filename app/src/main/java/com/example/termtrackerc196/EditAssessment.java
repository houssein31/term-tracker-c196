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

import com.example.termtrackerc196.db.DAOs.AssessmentDAO;
import com.example.termtrackerc196.db.DAOs.CourseDAO;
import com.example.termtrackerc196.db.DatabaseConn;
import com.example.termtrackerc196.db.Entities.Assessment;
import com.example.termtrackerc196.db.Entities.Course;

import java.util.Calendar;
import java.util.List;

public class EditAssessment extends AppCompatActivity {

    private EditText assessmentTitleInput;
    private RadioGroup assessmentTypeInput;
    private RadioGroup assessmentStatusInput;
    private Button assessmentEndDateInput;
    private EditText assessmentNoteInput;
    private int courseID;
    CourseDAO courseDAO;

    List<Course> allCourse;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    private DatePickerDialog assessmentEndDatePickerDialog;
    private Button assessmentEndDateButton;
    RadioGroup radioGroup;
    RadioButton perfomanceButton, objectiveButton, upcomingButton, inProgressButton, completedButton;
    String selectedTypeButton, selectedStatusButton;
    TextView textView;

    private int assessmentID;
    private AssessmentDAO assessmentDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);

        assessmentTitleInput = findViewById(R.id.assessmentTitleInput);
        assessmentTypeInput = findViewById(R.id.asse_type_radiogroup);
        assessmentStatusInput = findViewById(R.id.asse_status_radiogroup);
        assessmentEndDateInput = findViewById(R.id.assessmentEndDatePickerButton);
        assessmentNoteInput = findViewById(R.id.assessmentNoteInput);

        courseDAO = DatabaseConn.getDBInstance(getApplicationContext()).getCourseDao();
        assessmentDAO = DatabaseConn.getDBInstance(getApplicationContext()).getAssessmentDao();

        assessmentID = getIntent().getIntExtra("assessmentID", -1);
        Assessment assessment = assessmentDAO.getAssessmentByID(assessmentID);

        assessmentTitleInput.setText(assessment.getAssessmentTitle());
        // Set the appropriate radio button based on the assessment type
        if (assessment.getAssessmentType().equals("Performance")) {
            assessmentTypeInput.check(R.id.radio_performance);
        } else {
            assessmentTypeInput.check(R.id.radio_objective);
        }

        // Set the appropriate radio button based on the assessment status
        if (assessment.getAssessmentStatus().equals("Upcoming")) {
            assessmentStatusInput.check(R.id.radio_asse_upcoming);
        } else if (assessment.getAssessmentStatus().equals("In Progress")) {
            assessmentStatusInput.check(R.id.radio_asse_in_progress);
        } else {
            assessmentStatusInput.check(R.id.radio_asse_completed);
        }
//        assessmentEndDateInput.setText(assessment.getAssessmentEndDate());

        assessmentEndDatePickerDialog = new DatePickerDialog(
                EditAssessment.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the end date text when the user chooses a date
                        assessmentEndDateInput.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        // Set the end date text to the assessment's current end date
        assessmentEndDateInput.setText(assessment.getAssessmentEndDate());

        // Set the onClickListener for the end date button
        assessmentEndDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the date picker dialog when the user clicks the button
                assessmentEndDatePickerDialog.show();
            }
        });

        assessmentNoteInput.setText(assessment.getAssessmentNote());

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        allCourse = courseDAO.getAllCourses();

        String[] courseTitles = new String[allCourse.size()];
        for (int i = 0; i < courseTitles.length; i++) {
            courseTitles[i] = allCourse.get(i).getCourseTitle();
        }

//        Arrays.stream(courseTitles).forEach(s -> {
//            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
//        });

        // Get the index of the course with the ID of the assessment's courseID
        int selectedIndex = -1;
        for (int i = 0; i < allCourse.size(); i++) {
            if (allCourse.get(i).getCourseID() == assessment.getCourseID()) {
                selectedIndex = i;
                break;
            }
        }

        autoCompleteTextView.setText(courseTitles[selectedIndex]);
        adapterItems = new ArrayAdapter<String>(this, R.layout.drop_down_item, courseTitles);
        autoCompleteTextView.setAdapter(adapterItems);


        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String assessmentTitle = assessmentTitleInput.getText().toString();
                int selectedTypeID = assessmentTypeInput.getCheckedRadioButtonId();
                String assessmentType = ((RadioButton) findViewById(selectedTypeID)).getText().toString();
                int selectedStatusID = assessmentStatusInput.getCheckedRadioButtonId();
                String assessmentStatus = ((RadioButton) findViewById(selectedStatusID)).getText().toString();
                String assessmentEndDate = assessmentEndDateInput.getText().toString();
                String assessmentNote = assessmentNoteInput.getText().toString();
                String selectedCourse = autoCompleteTextView.getText().toString();
                int courseID = courseDAO.getCourseIDByTitle(selectedCourse);

                assessmentDAO.updateAssessmentByID(assessmentID, assessmentTitle, assessmentType, assessmentStatus, assessmentEndDate, assessmentNote, courseID);

                Intent intent = new Intent(EditAssessment.this, DisplayAssessment.class);
                intent.putExtra("assessmentID", assessmentID);
                startActivity(intent);

            }
        });
    }

    private void editAddAssessment() {
        Intent i = new Intent(this, EditAssessment.class);
        startActivity(i);
        super.finish();
    }

    private void saveNewAssessment(String assessmentTitle, String assessmentType, String assessmentStatus, String assessmentEndDate, String assessmentNote) {

        String selectedCourse = autoCompleteTextView.getText().toString();

        courseID = courseDAO.getCourseIDByTitle(selectedCourse);


        Assessment assessment = new Assessment(courseID, assessmentTitle, assessmentType, assessmentStatus, assessmentEndDate, assessmentNote);


        assessmentDAO.insertAssessment(assessment);
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

        DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month= month + 1;
                String date = makeDateString(day, month, year);
                assessmentEndDateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        assessmentEndDatePickerDialog = new DatePickerDialog(this, style, endDateSetListener, year, month, day);

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


    public void openAssessmentEndDatePicker(View view) {
        assessmentEndDatePickerDialog.show();
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