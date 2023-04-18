package com.example.termtrackerc196;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.termtrackerc196.db.DAOs.CourseDAO;
import com.example.termtrackerc196.db.DAOs.TermDAO;
import com.example.termtrackerc196.db.DatabaseConn;
import com.example.termtrackerc196.db.Entities.Course;
import com.example.termtrackerc196.db.Entities.Term;

import java.util.List;

public class DisplayTerm extends AppCompatActivity {

    String type;
    RecyclerView recyclerView;
    TermDAO termDAO;
    CourseDAO courseDAO;
    Button addNewItemButton;
    private TermListAdapter termListAdapter;
    private  CourseListAdapter courseListAdapter;

    int termID;

    List<Term> termList;
    List<Course> courseList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_term);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            termID = extras.getInt("termID");
        }

        termDAO = DatabaseConn.getDBInstance(getApplicationContext()).getTermDao();
        courseDAO = DatabaseConn.getDBInstance(getApplicationContext()).getCourseDao();

        Term term = termDAO.getTermByID(termID);

        String termTitle = term.getTermTitle();
        String termStartDate = term.getTermStartDate();
        String termEndDate = term.getTermEndDate();

        TextView termTitleTextView = findViewById(R.id.termTitleTextView);
        TextView termStartDateTexView = findViewById(R.id.termStartDateTextView);
        TextView termEndDateTexView = findViewById(R.id.termEndDateTextView);

        termTitleTextView.setText(termTitle);
        termStartDateTexView.setText(termStartDate);
        termEndDateTexView.setText(termEndDate);

        setTitle(termTitle);

        recyclerView = findViewById(R.id.display_term_recyclerView);

        initRecyclerView();

        Button editTermButton = findViewById(R.id.edit_term_button);
        editTermButton.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayTerm.this, EditTerm.class);
            intent.putExtra("termID", termID);
            intent.putExtra("termName", termTitle);
            intent.putExtra("termStartDate", termStartDate);
            intent.putExtra("termEndDate", termEndDate);
            startActivity(intent);
        });
    }

    private void initRecyclerView() {

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        List<Course> courses = courseDAO.getCoursesForTermID(termID);

        courseListAdapter = new CourseListAdapter(this, courses, new RecyclerViewInterface() {
            @Override
            public void onItemClick(int position) {
                Intent intentCourse = new Intent(DisplayTerm.this, DisplayCourse.class);

                intentCourse.putExtra("courseID", courses.get(position).getCourseID());
                startActivity(intentCourse);
            }
        });

        recyclerView.setAdapter(courseListAdapter);

    }

    private void loadCourseList() {
        courseList = courseDAO.getAllCourses();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100) {
            loadCourseList();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onItemClick(int position) {

        Intent intentCourse = new Intent(DisplayTerm.this, DisplayCourse.class);

//                intentCourse.putExtra("TermTitle", courseList.get(position).getTermTitle());
        intentCourse.putExtra("CourseTitle", courseList.get(position).getCourseTitle());
        intentCourse.putExtra("CourseInstructorName", courseList.get(position).getCourseInstructorName());
        intentCourse.putExtra("CourseInstructorEmail", courseList.get(position).getCourseInstructorEmail());
        intentCourse.putExtra("CourseInstructorPhone", courseList.get(position).getCourseInstructorPhone());
        intentCourse.putExtra("CourseStatus", courseList.get(position).getCourseStatus());
        intentCourse.putExtra("CourseStartDate", courseList.get(position).getCourseStartDate());
        intentCourse.putExtra("CourseEndDate", courseList.get(position).getCourseEndDate());
        intentCourse.putExtra("CourseNote", courseList.get(position).getCourseNote());

        startActivity(intentCourse);

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
}