package com.example.termtrackerc196;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.termtrackerc196.db.DatabaseConn;
import com.example.termtrackerc196.db.Entities.Term;

public class AddNewTermActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_term);

        final EditText termTitleInput = findViewById(R.id.termTitleInput);
        final EditText startDateInput = findViewById(R.id.startDateInput);
        final EditText enDateInput = findViewById(R.id.endDateInput);
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewTerm(termTitleInput.getText().toString(), startDateInput.getText().toString(), enDateInput.getText().toString());
            }
        });

    }

    private void saveNewTerm(String termTitle, String termStartDate, String termEndDate) {
        DatabaseConn db = DatabaseConn.getDBInstance(this.getApplicationContext());

        Term term = new Term(termTitle, termStartDate.toString(), termEndDate.toString());
        term.setTermTitle(termTitle);
        term.setTermStartDate(termStartDate);
        term.setTermEndDate(termEndDate);
        db.getTermsDao().insertTerm(term);

        finish();
    }
}