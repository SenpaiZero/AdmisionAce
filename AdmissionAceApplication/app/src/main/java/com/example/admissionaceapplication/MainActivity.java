package com.example.admissionaceapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.admissionaceapplication.Activities.NumItemsActivity;
import com.example.admissionaceapplication.Activities.NumItemsActivityEng;
import com.example.admissionaceapplication.Activities.NumItemsActivityLog;
import com.example.admissionaceapplication.Activities.NumItemsActivitySc;

public class MainActivity extends AppCompatActivity {

    CardView mathematics, science, english, logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mathematics = findViewById(R.id.mathematics);
        science = findViewById(R.id.science);
        english = findViewById(R.id.english);
        logic = findViewById(R.id.logic);

        mathematics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open NumItemsActivity for mathematics
                Intent intent = new Intent(MainActivity.this, NumItemsActivity.class);
                startActivity(intent);
            }
        });

        // Add OnClickListener for the science card
        science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open ScienceActivity (replace ScienceActivity with the actual name of the activity for science)
                Intent intent = new Intent(MainActivity.this, NumItemsActivitySc.class);
                startActivity(intent);
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open ScienceActivity (replace ScienceActivity with the actual name of the activity for science)
                Intent intent = new Intent(MainActivity.this, NumItemsActivityEng.class);
                startActivity(intent);
            }
        });

        logic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open ScienceActivity (replace ScienceActivity with the actual name of the activity for science)
                Intent intent = new Intent(MainActivity.this, NumItemsActivityLog.class);
                startActivity(intent);
            }
        });
    }
}
