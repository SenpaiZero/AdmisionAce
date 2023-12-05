package com.example.admissionaceapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.admissionaceapplication.MainActivity;
import com.example.admissionaceapplication.R;
import com.example.admissionaceapplication.databinding.ActivityQuestionsBinding;
import com.example.admissionaceapplication.databinding.ActivityScoreBinding;
import com.google.android.material.color.utilities.Score;

public class ScoreActivity extends AppCompatActivity {

    ActivityScoreBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        TextView txt = findViewById(R.id.info);
        int totalScore = getIntent().getIntExtra("total",0);
        int correctAnsw = getIntent().getIntExtra("score", 0);

        int wrong = totalScore - correctAnsw;

        binding.totalQuestions.setText(String.valueOf(totalScore));
        binding.rightAnsw.setText(String.valueOf(correctAnsw));

        binding.wrongAnsw.setText(String.valueOf(wrong));
        if(correctAnsw >= 20)
            txt.setText("Absolutely flawless! You've achieved a perfect score");
        else if(correctAnsw > 15 && correctAnsw < 20)
            txt.setText("Well done! You've grasped the majority of the questions");
        else if(correctAnsw > 10 && correctAnsw < 15)
            txt.setText("Good effort! There's room for improvement. Focus on the areas where you struggled.");
        else if(correctAnsw < 10)
            txt.setText("Room for growth. Identify specific areas that need attention.");
        else
            txt.setText("");

        binding.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ScoreActivity.this,NumItemsActivity.class);
                startActivity(intent);
                finish();

            }
        });

        binding.btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

}
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
