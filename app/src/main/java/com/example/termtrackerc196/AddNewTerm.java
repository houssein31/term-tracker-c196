package com.example.termtrackerc196;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.termtrackerc196.db.DatabaseConn;
import com.example.termtrackerc196.db.Entities.Term;

import java.util.Calendar;

public class AddNewTerm extends AppCompatActivity {

    private DatePickerDialog startDatePickerDialog,endDatePickerDialog;
    private Button startDateButton;
    private Button endDateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_term);

        initDatePicker();
        startDateButton = findViewById(R.id.termStartDatePickerButton);
        endDateButton = findViewById(R.id.termEndDatePickerButton);

        startDateButton.setText(getTodaysDate());
        endDateButton.setText(getTodaysDate());

        final EditText termTitleInput = findViewById(R.id.termTitleInput);
        final Button startDateInput = findViewById(R.id.termStartDatePickerButton);
        final Button enDateInput = findViewById(R.id.termEndDatePickerButton);
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewTerm(termTitleInput.getText().toString(), startDateInput.getText().toString(), enDateInput.getText().toString());
            }
        });

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

    private void saveNewTerm(String termTitle, String termStartDate, String termEndDate) {
        DatabaseConn db = DatabaseConn.getDBInstance(this.getApplicationContext());

        Term term = new Term(termTitle, termStartDate.toString(), termEndDate.toString());
        term.setTermTitle(termTitle);
        term.setTermStartDate(termStartDate);
        term.setTermEndDate(termEndDate);
        db.getTermDao().insertTerm(term);

        launchMultiPageActivity();

        super.finish();
    }

    private void launchMultiPageActivity() {
        Intent i = new Intent(this, MultiPageActivity.class);
        i.putExtra("type", "Terms");
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



    public void openTermStartDatePicker(View view) {
        startDatePickerDialog.show();
    }

    public void openTermEndDatePicker(View view) {
        endDatePickerDialog.show();
    }
}