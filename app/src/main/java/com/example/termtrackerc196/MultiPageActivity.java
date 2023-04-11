package com.example.termtrackerc196;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtrackerc196.db.DAOs.CourseDAO;
import com.example.termtrackerc196.db.DAOs.TermDAO;
import com.example.termtrackerc196.db.DatabaseConn;
import com.example.termtrackerc196.db.Entities.Course;
import com.example.termtrackerc196.db.Entities.Term;

import java.util.List;

public class MultiPageActivity extends AppCompatActivity implements RecyclerViewInterface{

    String type;

    RecyclerView recyclerView;
    TermDAO termDAO;
    CourseDAO courseDAO;
    Button addNewItemButton;
    private TermListAdapter termListAdapter;
    private  CourseListAdapter courseListAdapter;

    List<Term> termList;
    List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_page);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            type = extras.getString("type");
        }

        termDAO = DatabaseConn.getDBInstance(getApplicationContext()).getTermsDao();
        courseDAO = DatabaseConn.getDBInstance(getApplicationContext()).getCourseDao();

        setTitle(type);

//        termDAO = DatabaseConn.getDBInstance(getApplicationContext()).getTermsDao();

        addNewItemButton = findViewById(R.id.add_new_item_button);
        addNewItemButton.setText("Add new " + type);

        recyclerView = findViewById(R.id.recyclerView);

//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//        });


        initRecyclerView(type);

    }

    public void addItem(View view){

        switch (type){
            case "Terms":
                launchAddTerm();
                break;
            case "Courses":
                launchAddCourses();
                break;
            case "Assessments":
                launchAddAssessments();
                break;
        }

    }

    private void launchAddAssessments() {


    }

    private void launchAddCourses() {
        Intent i = new Intent(this, AddNewCourseActivity.class);
        startActivity(i);
    }

    private void launchAddTerm() {
        Intent i = new Intent(this, AddNewTermActivity.class);
        startActivity(i);
    }


    private void initRecyclerView(String type) {

//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        switch (type){
            case "Terms":
                termList = termDAO.getAllTerms();
                Toast.makeText(this, termList.toString(), Toast.LENGTH_SHORT).show();
                termListAdapter = new TermListAdapter(this, termList, this);
                recyclerView.setAdapter(termListAdapter);
                break;
            case "Courses":
                //intiCourseRV
                courseList = courseDAO.getAllCourses();
                Toast.makeText(this, courseList.toString(), Toast.LENGTH_SHORT).show();
                courseListAdapter = new CourseListAdapter(this, courseList, this);
                recyclerView.setAdapter(courseListAdapter);
                break;
            case "assessment":
                //intiAssessmentRV
                break;
        }



    }

    private void loadTermList() {
        termList = termDAO.getAllTerms();
        //termListAdapter.setTermList(termList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100) {
            loadTermList();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(MultiPageActivity.this, DisplayTerm.class);

        intent.putExtra("TermTitle", termList.get(position).getTermTitle());
        intent.putExtra("TermStartDate", termList.get(position).getTermStartDate());
        intent.putExtra("TermEndDate", termList.get(position).getTermEndDate());

        startActivity(intent);
    }
}
