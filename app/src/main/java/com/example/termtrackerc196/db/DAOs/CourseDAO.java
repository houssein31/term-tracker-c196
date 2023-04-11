package com.example.termtrackerc196.db.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.termtrackerc196.db.Entities.Course;
import com.example.termtrackerc196.db.Entities.Term;

import java.util.List;

@Dao
public interface CourseDAO {

    @Insert
    void insertCourse(Course... course);

    @Query("SELECT * FROM course")
    List<Course> getAllCourses();

    @Delete
    void delete(Course course);
}
