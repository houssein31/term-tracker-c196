package com.example.termtrackerc196.db.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessment",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "course_ID",
                childColumns = "course_ID",
                onDelete = ForeignKey.CASCADE
        ))
public class Assessment {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "assessment_ID")
    @NonNull
    private int assessmentID;
    @ColumnInfo(name = "course_ID")
    private int courseID;
    @ColumnInfo(name = "assessment_title")
    private String assessmentTitle;
    @ColumnInfo(name = "assessment_type")
    private String assessmentType;
    @ColumnInfo(name = "assessment_status")
    private String assessmentStatus;
    @ColumnInfo(name = "assessment_end_date")
    private String assessmentEndDate;
    @ColumnInfo(name = "assessment_note")
    private String assessmentNote;

    public Assessment(int courseID, String assessmentTitle, String assessmentType, String assessmentStatus, String assessmentEndDate, String assessmentNote) {
        this.courseID = courseID;
        this.assessmentTitle = assessmentTitle;
        this.assessmentType = assessmentType;
        this.assessmentStatus = assessmentStatus;
        this.assessmentEndDate = assessmentEndDate;
        this.assessmentNote = assessmentNote;
    }

    public int getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(int assessmentID) {
        this.assessmentID = assessmentID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    public void setAssessmentTitle(String assessmentTitle) {
        this.assessmentTitle = assessmentTitle;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public String getAssessmentStatus() {
        return assessmentStatus;
    }

    public void setAssessmentStatus(String assessmentStatus) {
        this.assessmentStatus = assessmentStatus;
    }

    public String getAssessmentEndDate() {
        return assessmentEndDate;
    }

    public void setAssessmentEndDate(String assessmentEndDate) {
        this.assessmentEndDate = assessmentEndDate;
    }

    public String getAssessmentNote() {
        return assessmentNote;
    }

    public void setAssessmentNote(String assessmentNote) {
        this.assessmentNote = assessmentNote;
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "assessmentID=" + assessmentID +
                ", courseID=" + courseID +
                ", assessmentTitle='" + assessmentTitle + '\'' +
                ", assessmentEndDate=" + assessmentEndDate +
                ", assessmentNote='" + assessmentNote + '\'' +
                '}';
    }
}
