package com.example.admissionaceapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.admissionaceapplication.Adapters.SetAdapter;
import com.example.admissionaceapplication.MainActivity;
import com.example.admissionaceapplication.Models.SetModel;
import com.example.admissionaceapplication.R;
import com.example.admissionaceapplication.databinding.ActivityNumItemsMathBinding;

import java.util.ArrayList;

public class NumItemsActivity extends AppCompatActivity {

    ActivityNumItemsMathBinding binding;
    ArrayList<SetModel>list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNumItemsMathBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        list = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.setsRecy.setLayoutManager(linearLayoutManager);

        list.add(new SetModel("SET-1"));
        list.add(new SetModel("SET-2"));
        list.add(new SetModel("SET-3"));

        SetAdapter adapter = new SetAdapter(this,list, "Math");
        binding.setsRecy.setAdapter(adapter);

        ImageView imageView = findViewById(R.id.backBtn);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NumItemsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}