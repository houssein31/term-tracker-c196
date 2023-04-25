package com.example.termtrackerc196;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DisplayAssessment extends AppCompatActivity {

    String type;
    Button addNewItemButton;
    int assessmentID;
    CourseDAO courseDAO;
    Assessment assessment;
    AssessmentDAO assessmentDAO;
    List<Assessment> assessmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_assessment);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            assessmentID = extras.getInt("assessmentID");
        }

        courseDAO = DatabaseConn.getDBInstance(getApplicationContext()).getCourseDao();
        assessmentDAO = DatabaseConn.getDBInstance(getApplicationContext()).getAssessmentDao();

        assessment = assessmentDAO.getAssessmentByID(assessmentID);

        String courseTitle = courseDAO.getCourseTitleByCourseID(assessment.getCourseID());
        String courseInstructorName = courseDAO.getCourseInstructorNameByCourseID(assessment.getCourseID());
        String assessmentTitle = assessment.getAssessmentTitle();
        String assessmentType = assessment.getAssessmentType();
        String assessmentStatus = assessment.getAssessmentStatus();
        String assessmentStartDate = assessment.getAssessmentStartDate();
        String assessmentEndDate = assessment.getAssessmentEndDate();
        String assessmentNote = assessment.getAssessmentNote();

        TextView courseTitleTextView = findViewById(R.id.courseTitleTextView);
        TextView courseInstructorNameTextView = findViewById(R.id.courseInstructorNameTextView);
        TextView assessmentTitleTextView = findViewById(R.id.assessmentTitleTextView);
        TextView assessmentTypeTextView = findViewById(R.id.assessmentTypeTextView);
        TextView assessmentStatusTextView = findViewById(R.id.assessmentStatusTextView);
        TextView assessmentStartDateTextView = findViewById(R.id.assessmentStartDateTextView);
        TextView assessmentEndDateTextView = findViewById(R.id.assessmentEndDateTextView);
        TextView assessmentNoteTextView = findViewById(R.id.assessmentNoteTextView);

        courseTitleTextView.setText(courseTitle);
        courseInstructorNameTextView.setText(courseInstructorName);
        assessmentTitleTextView.setText(assessmentTitle);
        assessmentTypeTextView.setText(assessmentType);
        assessmentStatusTextView.setText(assessmentStatus);
        assessmentStartDateTextView.setText(assessmentStartDate);
        assessmentEndDateTextView.setText(assessmentEndDate);
        assessmentNoteTextView.setText(assessmentNote);

        setTitle(assessmentTitle);

        Button shareButton = findViewById(R.id.share_asse_note_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String assessmentNote = ((TextView) findViewById(R.id.assessmentNoteTextView)).getText().toString();

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
//                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "recipient@example.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Assessment Note");
                intent.putExtra(Intent.EXTRA_TEXT, assessmentNote);
                startActivity(Intent.createChooser(intent, ""));


                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });


        Button editAssessmentButton = findViewById(R.id.edit_assessment_button);
        editAssessmentButton.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayAssessment.this, EditAssessment.class);
            intent.putExtra("assessmentID", assessmentID);
            intent.putExtra("assessmentTitle", assessmentTitle);
            intent.putExtra("assessmentType", assessmentType);
            intent.putExtra("assessmentStatus", assessmentStatus);
            intent.putExtra("assessmentStartDate", assessmentStartDate);
            intent.putExtra("assessmentEndDate", assessmentEndDate);
            intent.putExtra("assessmentNote", assessmentNote);
            startActivity(intent);
        });

        notificationChan();

    }

    private void notificationChan(){
        CharSequence seqName = "Assessment alarm";
        String shortDescription = "Assessment alarm notification";
        int important = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel notifChannel = new NotificationChannel("Term tracker", seqName, important);
        notifChannel.setDescription(shortDescription);
        NotificationManager notifManager = getSystemService(NotificationManager.class);
        notifManager.createNotificationChannel(notifChannel);
    }

    public void setStartAlarmForAssessment(View view){
        String assessmentTitle = assessment.getAssessmentTitle();
//        scheduleNotification(assessment.getCourseID(), startDate, assessmentTitle, firstLine, secondLine);

        String startDate = assessment.getAssessmentStartDate();
        String firstLine = "Assessment Start Date";
        String secondLine = " starts on ";
        scheduleNotification(assessment.getAssessmentID(), startDate, assessmentTitle, firstLine, secondLine);
    }

    public void setEndAlarmForAssessment(View view){
        String assessmentTitle = assessment.getAssessmentTitle();
//        scheduleNotification(assessment.getCourseID(), startDate, assessmentTitle, firstLine, secondLine);

        String endDate = assessment.getAssessmentEndDate();
        String firstLine = "Assessment End Date";
        String secondLine = " ends on ";
        scheduleNotification(assessment.getAssessmentID(), endDate, assessmentTitle, firstLine, secondLine);
    }

    private void scheduleNotification(int courseID, String date, String courseTitle, String notificationTitle,String msg){
        long todaysDate = getTodaysDate();
        long notifDate = getTimeInMilliSec(date);
        if(todaysDate <= getTimeInMilliSec(date)){
            Toast.makeText(this, "Schedualing ... ", Toast.LENGTH_SHORT).show();
            NotificationReceiver.scheduleCourseNotification(getApplicationContext(), courseID, notifDate, notificationTitle, courseTitle + msg + date);
        }
    }

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    private static long getTimeInMilliSec(String dateVal){
        try{
            Date date = dateFormat.parse(dateVal + TimeZone.getDefault().getDisplayName());
            return date.getTime();
        }catch (ParseException e){
            return 0l;
        }
    }

    private static long getTodaysDate(){
        String todaysDate = dateFormat.format(new Date());
        return getTimeInMilliSec(todaysDate);
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