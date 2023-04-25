package com.example.termtrackerc196;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.example.termtrackerc196.db.DAOs.AssessmentDAO;
import com.example.termtrackerc196.db.DAOs.CourseDAO;
import com.example.termtrackerc196.db.DatabaseConn;
import com.example.termtrackerc196.db.Entities.Assessment;
import com.example.termtrackerc196.db.Entities.Course;

import java.util.Calendar;
import java.util.List;

public class AddNewAssessment extends AppCompatActivity {

    private int courseID;
    CourseDAO courseDAO;
    AssessmentDAO assessmentDAO;
    List<Course> allCourse;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    private DatePickerDialog assessmentStartDatePickerDialog, assessmentEndDatePickerDialog;
    private Button assessmentStartDateButton, assessmentEndDateButton;
    DatePickerDialog.OnDateSetListener assessmentStartDateSetListener, assessmentEndDateSetListener;

    RadioGroup radioGroup;
    RadioButton perfomanceButton, objectiveButton, upcomingButton, inProgressButton, completedButton;
    String selectedTypeButton, selectedStatusButton;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_assessment);

        assessmentDAO = DatabaseConn.getDBInstance(getApplicationContext()).getAssessmentDao();
        courseDAO = DatabaseConn.getDBInstance(getApplicationContext()).getCourseDao();

        assessmentStartDateButton = findViewById(R.id.assessmentStartDatePickerButton);
        assessmentEndDateButton = findViewById(R.id.assessmentEndDatePickerButton);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        perfomanceButton = (RadioButton)findViewById(R.id.radio_performance);
        objectiveButton = (RadioButton)findViewById(R.id.radio_objective);

        upcomingButton = (RadioButton)findViewById(R.id.radio_asse_upcoming);
        inProgressButton = (RadioButton)findViewById(R.id.radio_asse_in_progress);
        completedButton = (RadioButton)findViewById(R.id.radio_asse_completed);

        courseDAO = DatabaseConn.getDBInstance(getApplicationContext()).getCourseDao();
        assessmentDAO = DatabaseConn.getDBInstance(getApplicationContext()).getAssessmentDao();

        setTitle("Add New Assessment");

        allCourse = courseDAO.getAllCourses();

        String[] courseTitles = new String[allCourse.size()];
        for (int i = 0; i<courseTitles.length; i++) {
            courseTitles[i] = allCourse.get(i).getCourseTitle();
        }

        adapterItems = new ArrayAdapter<String>(this, R.layout.drop_down_item, courseTitles);

        autoCompleteTextView.setAdapter(adapterItems);
        //

        assessmentStartDatePickerDialog = new DatePickerDialog(
                AddNewAssessment.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the end date text when the user chooses a date
                        assessmentStartDateButton.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        assessmentEndDatePickerDialog = new DatePickerDialog(
                AddNewAssessment.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the end date text when the user chooses a date
                        assessmentEndDateButton.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        assessmentStartDateButton.setText(getTodaysDate());
        assessmentEndDateButton.setText(getTodaysDate());

        assessmentStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assessmentStartDatePickerDialog.show();
            }
        });

        assessmentEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assessmentEndDatePickerDialog.show();
            }
        });

        final EditText assessmentTitleInput = findViewById(R.id.assessmentTitleInput);
        final RadioGroup assessmentTypeInput = findViewById(R.id.asse_type_radiogroup);
        final RadioGroup assessmentStatusInput = findViewById(R.id.asse_status_radiogroup);
        final Button assessmentStartDateInput = findViewById(R.id.assessmentStartDatePickerButton);
        final Button assessmentEndDateInput = findViewById(R.id.assessmentEndDatePickerButton);
        final EditText assessmentNoteInput = findViewById(R.id.assessmentNoteInput);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (assessmentTypeInput.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select an assessment type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (assessmentStatusInput.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select an assessment status", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (assessmentStartDateInput.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter an assessment start date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (assessmentEndDateInput.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter an assessment end date", Toast.LENGTH_SHORT).show();
                    return;
                }




                if ((perfomanceButton.isChecked())) {
                    selectedTypeButton = perfomanceButton.getText().toString();
                } else if(objectiveButton.isChecked()) {
                    selectedTypeButton = objectiveButton.getText().toString();
                }

                if (upcomingButton.isChecked()) {
                    selectedStatusButton = upcomingButton.getText().toString();
                } else if(inProgressButton.isChecked()) {
                    selectedStatusButton = inProgressButton.getText().toString();
                } else if(completedButton.isChecked()) {
                    selectedStatusButton = completedButton.getText().toString();
                }


                String startDate = assessmentStartDateInput.getText().toString().trim();
                if (startDate.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select a start date", Toast.LENGTH_SHORT).show();
                    return;
                }

                String endDate = assessmentEndDateInput.getText().toString().trim();
                if (endDate.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select an end date", Toast.LENGTH_SHORT).show();
                    return;
                }


                saveNewCourse(assessmentTitleInput.getText().toString(), selectedTypeButton, selectedStatusButton, assessmentStartDateInput.getText().toString(), assessmentEndDateInput.getText().toString(), assessmentNoteInput.getText().toString());

            }
        });



    }
    private void saveNewCourse(String assessmentTitle, String assessmentType, String assessmentStatus, String assessmentStartDate, String assessmentEndDate, String assessmentNote) {

        String selectedCourse = autoCompleteTextView.getText().toString();
        if (selectedCourse.trim().isEmpty() | selectedCourse.equals("Choose a Course") ) {
            Toast.makeText(this, "Please select a Course.", Toast.LENGTH_SHORT).show();
            return;
        }

        courseID = courseDAO.getCourseIDByTitle(selectedCourse);

        Assessment assessment = new Assessment(courseID, assessmentTitle, assessmentType, assessmentStatus, assessmentStartDate, assessmentEndDate, assessmentNote);

        assessmentDAO.insertAssessment(assessment);
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

    private void initDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        assessmentEndDatePickerDialog = new DatePickerDialog(AddNewAssessment.this, assessmentEndDateSetListener, year, month, day);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                android.R.style.Theme_DeviceDefault_Dialog,
                assessmentEndDateSetListener,
                year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }


    private String makeDateString(int year, int month, int day) {
        return year + "/" + month + "/" + day;
    }

    public void openAssessmentStartDatePicker(View view) {
        assessmentStartDatePickerDialog.show();
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