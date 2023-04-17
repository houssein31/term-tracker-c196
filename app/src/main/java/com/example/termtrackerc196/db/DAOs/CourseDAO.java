package com.example.termtrackerc196.db.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.termtrackerc196.db.Entities.Course;

import java.util.List;

@Dao
public interface CourseDAO {

    @Insert
    void insertCourse(Course... course);

    @Query("SELECT * FROM course")
    List<Course> getAllCourses();

    @Query("SELECT * FROM course WHERE term_ID = :termID")
    List<Course> getCoursesForTermID(int termID);

    @Query("SELECT * FROM course WHERE course_ID = :courseID")
    Course getCourseByID(int courseID);

    @Query("SELECT course_title FROM course WHERE course_ID = :courseID")
    String getCourseTitleByCourseID(int courseID);

    @Query("SELECT course_instructor_name FROM course WHERE course_ID = :courseID")
    String getCourseInstructorNameByCourseID(int courseID);
    @Query("DELETE FROM course WHERE course_id = :courseID")
    void delete(int courseID);

    @Query("SELECT course_ID FROM course WHERE course_title = :title")
    int getCourseIDByTitle(String title);

    @Query("SELECT COUNT(*) > 0 AS has_assessments FROM assessment WHERE course_ID = :courseID")
    boolean hasTerm(int courseID);

    @Query("SELECT COUNT(*) > 0 AS has_assessments FROM assessment WHERE course_ID = :courseID")
    boolean hasAssessment(int courseID);

    @Query("UPDATE course SET course_title = :title, course_instructor_name = :courseInstructorName, course_instructor_email = :courseInstructorEmail, course_instructor_phone = :courseInstructorPhone, course_status = :courseStatus, course_start_date = :courseStartDate, course_end_date = :courseEndDate , course_note = :courseNote, term_ID = :termID WHERE course_id = :id")
    void updateCourseByID(int id, String title, String courseInstructorName, String courseInstructorEmail, String courseInstructorPhone, String courseStatus, String courseStartDate, String courseEndDate, String courseNote, int termID);

//    @Query("UPDATE course SET course_title = :courseTitle, course_instructor_name = :courseInstructorName, course_instructor_email = :courseInstructorEmail, course_instructor_phone = :courseInstructorPhone, course_status = :courseStatus, course_start_date = :courseStartDate, course_end_date = :courseEndDate , course_note = :courseNote, term_ID = :termID WHERE term_id = :id")
//    void updateCourseByID(int id, String courseTitle, String courseInstructorName, String courseInstructorEmail, String courseInstructorPhone, String courseStatus, String courseStartDate, String courseEndDate, String courseNote, int termID);

}
