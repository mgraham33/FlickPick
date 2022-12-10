package com.example.androidtest4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Activity1 extends AppCompatActivity {


    private TextView name1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        name1 = findViewById(R.id.name1);
        String name = getIntent().getStringExtra("keyname");
        name1.setText(name);
    }
}