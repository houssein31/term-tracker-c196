package com.example.termtrackerc196.db.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "term")
public class Term {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "term_ID")
    @NonNull
    private int termId;
    @ColumnInfo(name = "term_title")
    private String termTitle;
    @ColumnInfo(name = "term_start_date")
    private String termStartDate;
    @ColumnInfo(name = "term_end_date")
    private String termEndDate;

    public Term(String termTitle, String termStartDate, String termEndDate) {
        this.termTitle = termTitle;
        this.termStartDate = termStartDate;
        this.termEndDate = termEndDate;
    }


    public int getTermId() {
        return termId;
    }

    public void setTermId(int id) {
        this.termId = id;
    }

    public String getTermTitle() {
        return termTitle;
    }

    public void setTermTitle(String termTitle) {
        this.termTitle = termTitle;
    }

    public String getTermStartDate() {
        return termStartDate;
    }

    public void setTermStartDate(String startDate) {
        this.termStartDate = startDate;
    }

    public String getTermEndDate() {
        return termEndDate;
    }

    public void setTermEndDate(String endDate) {
        this.termEndDate = endDate;
    }

    @Override
    public String toString() {
        return "term{" +
                "termId=" + termId +
                ", termTitle='" + termTitle + '\'' +
                ", termStartDate=" + termStartDate +
                ", termEndDate=" + termEndDate +
                '}';
    }
}
