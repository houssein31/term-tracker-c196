package com.example.termtrackerc196;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class DisplayTerm extends AppCompatActivity {

    String type;
    Button addNewItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_term);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            type = extras.getString("type");
        }


        String termTitle = getIntent().getStringExtra("TermTitle");
        String termStartDate = getIntent().getStringExtra("TermStartDate");
        String termEndDate = getIntent().getStringExtra("TermEndDate");

        TextView termTitleTextView = findViewById(R.id.termTitleTextView);
        TextView termStartDateTexView = findViewById(R.id.termStartDateTextView);
        TextView termEndDateTexView = findViewById(R.id.termEndDateTextView);

        termTitleTextView.setText(termTitle);
        termStartDateTexView.setText(termStartDate);
        termEndDateTexView.setText(termEndDate);

        setTitle(termTitle);

        addNewItemButton = findViewById(R.id.add_new_item_button);
        addNewItemButton.setText("Add new " + type);


    }
}