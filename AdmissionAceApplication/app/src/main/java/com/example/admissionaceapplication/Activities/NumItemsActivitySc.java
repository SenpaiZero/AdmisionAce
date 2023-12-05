package com.example.admissionaceapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.admissionaceapplication.Adapters.SetAdapter;
import com.example.admissionaceapplication.MainActivity;
import com.example.admissionaceapplication.Models.SetModel;
import com.example.admissionaceapplication.R;
import com.example.admissionaceapplication.databinding.ActivityNumItemsMathBinding;
import com.example.admissionaceapplication.databinding.ActivityNumItemsScBinding;

import java.util.ArrayList;

public class NumItemsActivitySc extends AppCompatActivity {

    ActivityNumItemsScBinding binding;
    ArrayList<SetModel>list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNumItemsScBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        list = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.setsRecy.setLayoutManager(linearLayoutManager);

        list.add(new SetModel("SET-1"));
        list.add(new SetModel("SET-2"));
        list.add(new SetModel("SET-3"));

        SetAdapter adapter = new SetAdapter(this,list, "Science");
        binding.setsRecy.setAdapter(adapter);

        ImageView imageView = findViewById(R.id.backBtn);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NumItemsActivitySc.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}