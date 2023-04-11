package com.example.termtrackerc196.db.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.termtrackerc196.db.Entities.Term;

import java.util.List;

@Dao
public interface TermDAO {

    @Insert
    void insertTerm(Term... term);

    @Query("SELECT * FROM term")
    List<Term> getAllTerms();

    @Query("SELECT term_ID FROM term WHERE term_title = :title")
    int getTermIDByTitle(String title);

    @Query("SELECT term_title FROM term")
    List<String> getAllTermTitles();

    @Delete
    void delete(Term term);

}
