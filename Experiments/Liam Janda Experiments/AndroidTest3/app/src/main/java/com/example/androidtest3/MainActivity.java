package com.example.androidtest3;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void generateText(View v){
        TextView adj = findViewById(R.id.enterAdj);
        TextView noun = findViewById(R.id.enterNoun);
        TextView verb = findViewById(R.id.enterVerb);
        String adjStr = adj.getText().toString();
        String nounStr = noun.getText().toString();
        String verbStr = verb.getText().toString();
        TextView resultText = findViewById(R.id.resultText);
        resultText.setText("The " + adjStr + " dog will " + verbStr + " over the lazy " + nounStr);


    }
}