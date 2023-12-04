package com.example.admissionaceapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.admissionaceapplication.Adapters.SetAdapter;
import com.example.admissionaceapplication.MainActivity;
import com.example.admissionaceapplication.Models.SetModel;
import com.example.admissionaceapplication.R;
import com.example.admissionaceapplication.databinding.ActivityNumItemsEngBinding;

import java.util.ArrayList;

public class NumItemsActivityEng extends AppCompatActivity {

    ActivityNumItemsEngBinding binding;
    ArrayList<SetModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNumItemsEngBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        list = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.setsRecy.setLayoutManager(linearLayoutManager);

        list.add(new SetModel("SET-1"));
        list.add(new SetModel("SET-2"));
        list.add(new SetModel("SET-3"));

        SetAdapter adapter = new SetAdapter(this,list, "English");
        binding.setsRecy.setAdapter(adapter);

        ImageView imageView = findViewById(R.id.backBtn);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NumItemsActivityEng.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}