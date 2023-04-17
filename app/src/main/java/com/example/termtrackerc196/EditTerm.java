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

public class EditTerm extends AppCompatActivity {

    private EditText termTitleInput;

    private int termID;
    TermDAO termDAO;

    List<Term> allTerm;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    private DatePickerDialog termStartDatePickerDialog, termEndDatePickerDialog;
    private Button termStartDateButton;
    private Button termEndDateButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);

        termTitleInput = findViewById(R.id.termTitleInput);
        termStartDateButton = findViewById(R.id.termStartDatePickerButton);
        termEndDateButton = findViewById(R.id.termEndDatePickerButton);

        termDAO = DatabaseConn.getDBInstance(getApplicationContext()).getTermDao();

        termID = getIntent().getIntExtra("termID", -1);
        Term term = termDAO.getTermByID(termID);


        termTitleInput.setText(term.getTermTitle());

        termStartDatePickerDialog = new DatePickerDialog(
                EditTerm.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the end date text when the user chooses a date
                        termStartDateButton.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        termEndDatePickerDialog = new DatePickerDialog(
                EditTerm.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the end date text when the user chooses a date
                        termEndDateButton.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        // Set the end date text to the assessment's current end date
        termStartDateButton.setText(term.getTermStartDate());
        termEndDateButton.setText(term.getTermEndDate());

        // Set the onClickListener for the end date button
        termStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the date picker dialog when the user clicks the button
                termStartDatePickerDialog.show();
            }
        });

        // Set the onClickListener for the end date button
        termEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the date picker dialog when the user clicks the button
                termEndDatePickerDialog.show();
            }
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String termTitle = termTitleInput.getText().toString();

                String termStartDate = termStartDateButton.getText().toString();

                String termEndDate = termEndDateButton.getText().toString();

                termDAO.updateTermByID(termID, termTitle, termStartDate, termEndDate);

                Intent intent = new Intent(EditTerm.this, DisplayTerm.class);
                intent.putExtra("termID", termID);
                startActivity(intent);
            }
        });

    }

    private void editTerm() {
        Intent i = new Intent(this, EditTerm.class);
        startActivity(i);
        super.finish();
    }

    private void saveNewTerm(String termTitle, String termStartDate, String termEndDate) {


        Term term = new Term(termTitle, termStartDate, termEndDate);


        termDAO.insertTerm(term);
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
                termStartDateButton.setText(date);
            }
        };

        DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month= month + 1;
                String date = makeDateString(day, month, year);
                termEndDateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        termStartDatePickerDialog = new DatePickerDialog(this, style, startDateSetListener, year, month, day);
        termEndDatePickerDialog = new DatePickerDialog(this, style, endDateSetListener, year, month, day);
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
        termStartDatePickerDialog.show();
    }
    public void openCourseEndDatePicker(View view) {
        termEndDatePickerDialog.show();
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