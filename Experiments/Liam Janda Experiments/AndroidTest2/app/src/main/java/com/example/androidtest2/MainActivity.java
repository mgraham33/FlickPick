package com.example.androidtest2;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    int health = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void add1(View v){
        ++health;
        ((TextView)findViewById(R.id.healthText)).setText(((Integer)health).toString());
    }

    public void add5(View v){
        health = health + 5;
        ((TextView)findViewById(R.id.healthText)).setText(((Integer)health).toString());
    }

    public void sub1(View v){
        --health;
        ((TextView)findViewById(R.id.healthText)).setText(((Integer)health).toString());
    }

    public void sub5(View v){
        health = health - 5;
        ((TextView)findViewById(R.id.healthText)).setText(((Integer)health).toString());
    }

    public void resetHealth(View v){
        health = 20;
        ((TextView)findViewById(R.id.healthText)).setText(((Integer)health).toString());
    }
}