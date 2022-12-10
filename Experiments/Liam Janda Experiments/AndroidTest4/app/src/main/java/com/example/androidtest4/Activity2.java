package com.example.androidtest4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Activity2 extends AppCompatActivity {

    TextView name2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        name2 = findViewById(R.id.name2);

        String name = getIntent().getStringExtra("keyname");
        name2.setText(name);
    }
}