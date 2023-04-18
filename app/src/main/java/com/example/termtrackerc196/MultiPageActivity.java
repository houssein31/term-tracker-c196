package com.example.termtrackerc196;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtrackerc196.db.DAOs.AssessmentDAO;
import com.example.termtrackerc196.db.DAOs.CourseDAO;
import com.example.termtrackerc196.db.DAOs.TermDAO;
import com.example.termtrackerc196.db.DatabaseConn;
import com.example.termtrackerc196.db.Entities.Assessment;
import com.example.termtrackerc196.db.Entities.Course;
import com.example.termtrackerc196.db.Entities.Term;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MultiPageActivity extends AppCompatActivity implements RecyclerViewInterface{

    String type;

    RecyclerView recyclerView;
    TermDAO termDAO;
    CourseDAO courseDAO;
    AssessmentDAO assessmentDAO;
    Button addNewItemButton;
    private TermListAdapter termListAdapter;
    private  CourseListAdapter courseListAdapter;
    private AssessmentListAdapter assessmentListAdapter;

    List<Term> termList;
    List<Course> courseList;
    List<Assessment> assessmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_page);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            type = extras.getString("type");
        }

        termDAO = DatabaseConn.getDBInstance(getApplicationContext()).getTermDao();
        courseDAO = DatabaseConn.getDBInstance(getApplicationContext()).getCourseDao();
        assessmentDAO = DatabaseConn.getDBInstance(getApplicationContext()).getAssessmentDao();

        setTitle(type);

        addNewItemButton = findViewById(R.id.add_new_item_button);
        addNewItemButton.setText("Add new " + type);

        recyclerView = findViewById(R.id.recyclerView);

        initRecyclerView(type);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (type){
                case "Terms":

                    int termID = termList.get(position).getTermId();

                    if (termDAO.hasCourse(termID)) {
                        // Show a pop-up dialog if the term contains courses
                        AlertDialog.Builder builder = new AlertDialog.Builder(MultiPageActivity.this);
                        builder.setMessage("This term cannot be deleted because it has courses associated with it. Delete all courses associated with this term and then delete it.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        initRecyclerView(type);

                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {
                        termList.remove(position);
                        termListAdapter.notifyItemRemoved(position);
                        termDAO.delete(termID);
                    }

                    break;
                case "Courses":

                    int courseID = courseList.get(position).getCourseID();

                    if (courseDAO.hasAssessment(courseID)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MultiPageActivity.this);
                        builder.setMessage("This course cannot be deleted because it has assessments associated with it. Delete all assessment associated with this course and then delete it.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        initRecyclerView(type);

                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {
                        courseList.remove(position);
                        courseListAdapter.notifyItemRemoved(position);
                        courseDAO.delete(courseID);
                    }
                    break;
                case "Assessments":
                    int assessmentID = assessmentList.get(position).getAssessmentID();
                    assessmentList.remove(position);
                    assessmentListAdapter.notifyItemRemoved(position);
                    assessmentDAO.delete(assessmentID);
                    break;
            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MultiPageActivity.this, R.color.colorAccent))
                    .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)

                    .create()
                    .decorate();


            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

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
        Intent i = new Intent(this, AddNewAssessment.class);
        startActivity(i);
        super.finish();
    }

    private void launchAddCourses() {
        Intent i = new Intent(this, AddNewCourse.class);
        startActivity(i);
        super.finish();
    }

    private void launchAddTerm() {
        Intent i = new Intent(this, AddNewTerm.class);
        startActivity(i);
        super.finish();
    }


    private void initRecyclerView(String type) {

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        switch (type){
            case "Terms":
                termList = termDAO.getAllTerms();
                termListAdapter = new TermListAdapter(this, termList, this);
                recyclerView.setAdapter(termListAdapter);
                break;
            case "Courses":
                courseList = courseDAO.getAllCourses();
                courseListAdapter = new CourseListAdapter(this, courseList, this);
                recyclerView.setAdapter(courseListAdapter);
                break;
            case "Assessments":
                assessmentList = assessmentDAO.getAllAssessments();
                assessmentListAdapter = new AssessmentListAdapter(this, assessmentList, this);
                recyclerView.setAdapter(assessmentListAdapter);
                break;
        }

    }

    private void loadTermList() { termList = termDAO.getAllTerms(); }
    private void loadCourseList() {
        courseList = courseDAO.getAllCourses();
    }

    private void loadAssessmentList() { assessmentList = assessmentDAO.getAllAssessments(); }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100) {
            loadTermList();
            loadCourseList();
            loadAssessmentList();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(int position) {

        switch (type){
            case "Terms":
                Intent intentTerm = new Intent(MultiPageActivity.this, DisplayTerm.class);

                intentTerm.putExtra("termID", termList.get(position).getTermId());

                startActivity(intentTerm);
                break;
            case "Courses":
                Intent intentCourse = new Intent(MultiPageActivity.this, DisplayCourse.class);

                intentCourse.putExtra("courseID", courseList.get(position).getCourseID());
                startActivity(intentCourse);
                super.finish();
                break;
            case "Assessments":
                Intent intentAssessment = new Intent(MultiPageActivity.this, DisplayAssessment.class);

                intentAssessment.putExtra("assessmentID", assessmentList.get(position).getAssessmentID());
                startActivity(intentAssessment);
                break;
        }
    }


}
