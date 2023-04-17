package com.example.termtrackerc196;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.termtrackerc196.db.DAOs.AssessmentDAO;
import com.example.termtrackerc196.db.DAOs.CourseDAO;
import com.example.termtrackerc196.db.DAOs.TermDAO;
import com.example.termtrackerc196.db.DatabaseConn;
import com.example.termtrackerc196.db.Entities.Assessment;
import com.example.termtrackerc196.db.Entities.Course;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DisplayCourse extends AppCompatActivity {

    String type;
    Button addNewItemButton;
    RecyclerView recyclerView;

    int courseID;
    TermDAO termDAO;
    CourseDAO courseDAO;
    AssessmentDAO assessmentDAO;

    Course course;
    List<Assessment> assessmentList;


    private  CourseListAdapter courseListAdapter;

    private  AssessmentListAdapter assessmentListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_course);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            courseID = extras.getInt("courseID");
        }

        termDAO = DatabaseConn.getDBInstance(getApplicationContext()).getTermDao();
        courseDAO = DatabaseConn.getDBInstance(getApplicationContext()).getCourseDao();
        assessmentDAO = DatabaseConn.getDBInstance(getApplicationContext()).getAssessmentDao();

        course = courseDAO.getCourseByID(courseID);

        String termTitle = termDAO.getTermTitleByTermID(course.getTermID());
        String courseTitle = course.getCourseTitle();
        String courseInstructorName = course.getCourseInstructorName();
        String courseInstructorEmail = course.getCourseInstructorEmail();
        String courseInstructorPhone = course.getCourseInstructorPhone();
        String courseStatus = course.getCourseStatus();
        String courseStartDate = course.getCourseStartDate();
        String courseEndDate = course.getCourseEndDate();
        String courseNote = course.getCourseNote();

        TextView termTitleTextView = findViewById(R.id.termTitleTextView);
        TextView courseTitleTextView = findViewById(R.id.courseTitleTextView);
        TextView courseInstructorNameTextView = findViewById(R.id.courseInstructorNameTextView);
        TextView courseInstructorEmailTextView = findViewById(R.id.courseInstructorEmailTextView);
        TextView courseInstructorPhoneTextView = findViewById(R.id.courseInstructorPhoneTextView);
        TextView courseStatusTextView = findViewById(R.id.courseStatusTextView);
        TextView courseStartDateTexView = findViewById(R.id.courseStartDateTextView);
        TextView courseEndDateTexView = findViewById(R.id.courseEndDateTextView);
        TextView courseNoteTextView = findViewById(R.id.courseNoteTextView);

//        Toolbar toolbar =findViewById(R.id.my_toolbar);
//        setSupportActionBar(toolbar);

        termTitleTextView.setText(termTitle);
        courseTitleTextView.setText(courseTitle);
        courseInstructorNameTextView.setText(courseInstructorName);
        courseInstructorEmailTextView.setText(courseInstructorEmail);
        courseInstructorPhoneTextView.setText(courseInstructorPhone);
        courseStatusTextView.setText(courseStatus);
        courseStartDateTexView.setText(courseStartDate);
        courseEndDateTexView.setText(courseEndDate);
        courseNoteTextView.setText(courseNote);

        setTitle(courseTitle);

//        addNewItemButton = findViewById(R.id.edit_course_button);
//        addNewItemButton.setText("Add new " + type);

        recyclerView = findViewById(R.id.display_course_recyclerView);

        initRecyclerView();

        Button editCourseButton = findViewById(R.id.edit_course_button);
        editCourseButton.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayCourse.this, EditCourse.class);
            intent.putExtra("courseID", courseID);
            intent.putExtra("courseInstructorName", courseInstructorName);
            intent.putExtra("courseInstructorEmail", courseInstructorEmail);
            intent.putExtra("courseInstructorPhone", courseInstructorPhone);
            intent.putExtra("courStatus", courseStatus);
            intent.putExtra("courseStartDate", courseStartDate);
            intent.putExtra("courseEndDate", courseEndDate);
            intent.putExtra("courseNote", courseNote);
            startActivity(intent);
            super.finish();
        });

        notificationChannel();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.my_menu, menu);
//        return true;
//    }

    private void initRecyclerView() {

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        List<Assessment> assessments = assessmentDAO.getAssessmentsForCourseID(courseID);

        assessmentListAdapter = new AssessmentListAdapter(this, assessments, new RecyclerViewInterface() {
            @Override
            public void onItemClick(int position) {
//                Toast.makeText(DisplayTerm.this, "On Item Click message", Toast.LENGTH_SHORT).show();

                Intent intentAssessment = new Intent(DisplayCourse.this, DisplayAssessment.class);

                intentAssessment.putExtra("assessmentID", assessments.get(position).getAssessmentID());
                startActivity(intentAssessment);
            }
        });

        recyclerView.setAdapter(assessmentListAdapter);


    }

    private void notificationChannel(){
        CharSequence name = "Course alarm";
        String description = "Course alarm notification";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel("Term tracker", name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void setCourseAlarm(View view){
        String date = course.getCourseStartDate();
        String alarmTitle = "THIS IS THE TITLE ";
        String courseTitle = course.getCourseTitle();
        String message = " THIS IS THE MESSAGE ";

        scheduleNotification(course.getCourseID(), date, courseTitle, alarmTitle, message);

        date = course.getCourseEndDate();
        alarmTitle = " THIS IS THE 2ND TITLE ";
        message = " THIS IS THE 2ND MESSAGE ";
        scheduleNotification(course.getCourseID(), date, courseTitle, alarmTitle, message);


    }

    private void scheduleNotification(int courseID, String date, String courseTitle, String notificationTitle,String msg){
        long currentDate = getCurrentDate();
        long notificationDate = getTimeInMilliSeconds(date);
        if(currentDate <= getTimeInMilliSeconds(date)){
            Toast.makeText(this, "HERE!!!!!", Toast.LENGTH_SHORT).show();
            NotificationReceiver.scheduleCourseNotification(getApplicationContext(), courseID, notificationDate, notificationTitle, courseTitle + msg + date);
        }
    }

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    private static long getTimeInMilliSeconds(String dateVal){
        try{
            Date date = dateFormat.parse(dateVal + TimeZone.getDefault().getDisplayName());
            return date.getTime();
        }catch (ParseException e){
            return 0l;
        }
    }

    private static long getCurrentDate(){
        String currentDate = dateFormat.format(new Date());
        return getTimeInMilliSeconds(currentDate);
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