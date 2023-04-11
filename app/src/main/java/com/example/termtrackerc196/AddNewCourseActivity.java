package com.example.termtrackerc196;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.termtrackerc196.db.DAOs.CourseDAO;
import com.example.termtrackerc196.db.DAOs.TermDAO;
import com.example.termtrackerc196.db.DatabaseConn;
import com.example.termtrackerc196.db.Entities.Course;
import com.example.termtrackerc196.db.Entities.Term;

import java.util.List;

public class AddNewCourseActivity extends AppCompatActivity {

    private int termID;

    TermDAO termDAO;
    CourseDAO courseDAO;

    List<Term> allTerm;

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);


        //Create termDAO instance through DatabaseConn abstract class because TermDAO does not have a constructor
        termDAO = DatabaseConn.getDBInstance(getApplicationContext()).getTermsDao();
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
        final EditText courseStatusInput = findViewById(R.id.courseStatusInput);
        final EditText courseStartDateInput = findViewById(R.id.courseStartDateInput);
        final EditText courseEndDateInput = findViewById(R.id.courseEndDateInput);
        final EditText courseNoteInput = findViewById(R.id.courseNoteInput);
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewCourse(courseTitleInput.getText().toString(), courseInstructorNameInput.getText().toString(), courseInstructorEmailInput.getText().toString(), courseInstructorPhoneInput.getText().toString(), courseStatusInput.getText().toString(), courseStartDateInput.getText().toString(), courseEndDateInput.getText().toString(), courseNoteInput.getText().toString());
            }
        });

    }

    private void saveNewCourse(String courseTitle, String courseInstructorName, String courseInstructorEmail, String courseInstructorPhone, String courseStatus, String courseStartDate, String courseEndDate, String courseNote) {

        String selectedTerm = autoCompleteTextView.getText().toString();

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
        finish();

    }

}

