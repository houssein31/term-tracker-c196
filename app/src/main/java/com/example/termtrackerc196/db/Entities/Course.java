package com.example.termtrackerc196.db.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "course",
        foreignKeys = @ForeignKey(
                entity = Term.class,
                parentColumns = "term_ID",
                childColumns = "term_ID"
        ))
public class Course {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "course_ID")
    @NonNull
    private int courseID;
    @ColumnInfo(name = "term_ID")
    private int termID;
    @ColumnInfo(name = "course_title")
    private String courseTitle;
    @ColumnInfo(name = "course_instructor_name")
    private String courseInstructorName;
    @ColumnInfo(name = "course_instructor_email")
    private String courseInstructorEmail;
    @ColumnInfo(name = "course_instructor_phone")
    private String courseInstructorPhone;
    @ColumnInfo(name = "course_status")
    private String courseStatus;
    @ColumnInfo(name = "course_start_date")
    private String courseStartDate;
    @ColumnInfo(name = "course_end_date")
    private String courseEndDate;
    @ColumnInfo(name = "course_note")
    private String courseNote;

    public Course(int termID, String courseTitle, String courseInstructorName, String courseInstructorEmail, String courseInstructorPhone, String courseStatus, String courseStartDate, String courseEndDate, String courseNote) {
        this.termID = termID;
        this.courseTitle = courseTitle;
        this.courseInstructorName = courseInstructorName;
        this.courseInstructorEmail = courseInstructorEmail;
        this.courseInstructorPhone = courseInstructorPhone;
        this.courseStatus = courseStatus;
        this.courseStartDate = courseStartDate;
        this.courseEndDate = courseEndDate;
        this.courseNote = courseNote;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseInstructorName() {
        return courseInstructorName;
    }

    public void setCourseInstructorName(String courseInstructorName) {
        this.courseInstructorName = courseInstructorName;
    }

    public String getCourseInstructorEmail() {
        return courseInstructorEmail;
    }

    public void setCourseInstructorEmail(String courseInstructorEmail) {
        this.courseInstructorEmail = courseInstructorEmail;
    }

    public String getCourseInstructorPhone() {
        return courseInstructorPhone;
    }

    public void setCourseInstructorPhone(String courseInstructorPhone) {
        this.courseInstructorPhone = courseInstructorPhone;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getCourseStartDate() {
        return courseStartDate;
    }

    public void setCourseStartDate(String courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public String getCourseEndDate() {
        return courseEndDate;
    }

    public void setCourseEndDate(String courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    public String getCourseNote() {
        return courseNote;
    }

    public void setCourseNote(String courseNote) {
        this.courseNote = courseNote;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseID=" + courseID +
                ", termID=" + termID +
                ", courseTitle='" + courseTitle + '\'' +
                ", courseInstructorName='" + courseInstructorName + '\'' +
                ", courseInstructorEmail='" + courseInstructorEmail + '\'' +
                ", courseInstructorPhone='" + courseInstructorPhone + '\'' +
                ", courseStatus='" + courseStatus + '\'' +
                ", courseStartDate=" + courseStartDate +
                ", courseEndDate=" + courseEndDate +
                ", courseNote='" + courseNote + '\'' +
                '}';
    }
}
