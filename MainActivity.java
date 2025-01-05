package com.example.a2024scoutingapp;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private boolean isOpen1 = false;
    private boolean isOpen2 = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button openLayoutButton1 = findViewById(R.id.endgame);
        Button openLayoutButton2 = findViewById(R.id.auto);
        openLayoutButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isOpen1 = !isOpen1;

                if(isOpen1) {
                    setContentView(R.layout.retryatendgame);
                }
                if(!isOpen1) {
                    setContentView(R.layout.activity_main);

                }
            }
        });
        openLayoutButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen2 = !isOpen2;

                if(isOpen2) {
                    setContentView(R.layout.auto);
                }
            }
        });
    }
}
