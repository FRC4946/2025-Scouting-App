package com.example.a2024scoutingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Credits extends AppCompatActivity {
    int SpeakerPoints;
    int AmpPoints;
    boolean AddPoints = true;
    //yes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);
        LinearLayout credits1 = findViewById(R.id.credits);
        credits1.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setContentView(R.layout.credits2);
                LinearLayout credits2 = findViewById(R.id.credits2);
                credits2.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        setContentView(R.layout.credits3);
                        LinearLayout credits3 = findViewById(R.id.credits3);
                        credits3.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                setContentView(R.layout.credits4);
                                LinearLayout credits4 = findViewById(R.id.credits4);
                                credits4.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {

                                        Intent intent = new Intent(Credits.this, MainActivity.class);
                                        startActivity(intent);
                                        return true;
                                    }
                                });
                                return true;
                            }
                        });
                        return true;
                    }

                });
                return true;
            }
        });
    }
}


