package com.example.termtrackerc196.db.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.termtrackerc196.db.Entities.Assessment;

import java.util.List;
@Dao
public interface AssessmentDAO {

    @Insert
    void insertAssessment(Assessment... assessment);

    @Query("SELECT * FROM assessment")
    List<Assessment> getAllAssessments();

    @Query("SELECT * FROM assessment WHERE course_ID = :courseID")
    List<Assessment> getAssessmentsForCourseID(int courseID);

    @Query("SELECT assessment_title FROM assessment WHERE assessment_ID = :assessmentID")
    String getAssessmentTitleByCourseID(int assessmentID);

    @Query("SELECT * FROM assessment WHERE assessment_ID = :assessmentID")
    Assessment getAssessmentByID(int assessmentID);

    @Query("SELECT assessment_ID FROM assessment WHERE assessment_title = :title")
    int getAssessmentsIDByTitle(String title);

    @Query("DELETE FROM assessment WHERE assessment_id = :assessmentID")
    void delete(int assessmentID);

    @Query("SELECT assessment_title FROM assessment WHERE assessment_id = :id")
    String getTitleById(int id);

    @Query("UPDATE assessment SET assessment_title = :title, assessment_type = :type, assessment_status = :status, assessment_end_date = :endDate, assessment_note = :note, course_ID = :courseID WHERE assessment_id = :id")
    void updateAssessmentByID(int id, String title, String type, String status, String endDate, String note, int courseID);

}
