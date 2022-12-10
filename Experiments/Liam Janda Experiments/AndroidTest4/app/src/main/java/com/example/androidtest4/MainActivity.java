package com.example.androidtest4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item1:
                name = (EditText)findViewById(R.id.name);
                Intent intent = new Intent(this, Activity1.class);
                intent.putExtra("keyname", name.getText().toString());
                startActivity(intent);
                return true;

            case R.id.item2:
                name = (EditText)findViewById(R.id.name);
                Intent intent2 = new Intent(this, Activity2.class);
                intent2.putExtra("keyname", name.getText().toString());
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}