package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        Intent intent = getIntent();
        String text = intent.getStringExtra(MainActivity.CHECK_TEXT);
        String color = intent.getStringExtra(MainActivity.CHECK_COLOR);
        String error = intent.getStringExtra(MainActivity.VALID_ERROR);

        TextView editable = (TextView) findViewById(R.id.checkText);
        editable.setText(text);
        editable.setTextColor(Color.parseColor(color));

        TextView error_msg = (TextView) findViewById(R.id.errorText);
        error_msg.setText(error);
    }

    public void check_back(View view)
    {
        super.finish();
    }

}