package com.example.termtrackerc196.db;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.termtrackerc196.db.DAOs.AssessmentDAO;
import com.example.termtrackerc196.db.DAOs.CourseDAO;
import com.example.termtrackerc196.db.DAOs.TermDAO;
import com.example.termtrackerc196.db.Entities.Assessment;
import com.example.termtrackerc196.db.Entities.Course;
import com.example.termtrackerc196.db.Entities.Term;

@Database(entities = {Term.class, Course.class, Assessment.class}, version = 1)
public abstract class DatabaseConn extends RoomDatabase {

    public abstract TermDAO getTermDao();
    public abstract CourseDAO getCourseDao();
    public abstract AssessmentDAO getAssessmentDao();

    private static DatabaseConn INSTANCE;

    public static DatabaseConn getDBInstance(Context context) {

        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseConn.class, "DB_NAME")
                    .allowMainThreadQueries()
                    .build();

        }
        return INSTANCE;
    }

}
