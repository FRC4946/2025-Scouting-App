package com.example.a2024scoutingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.a2024scoutingapp.forms.ScoutingForm;

public class MainActivity extends AppCompatActivity {
    private ScoutingForm m_currentForm = new ScoutingForm();
    private String m_loadName;
    private Button m_sendButton, m_nextButton, load;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        Intent intent = getIntent();
        m_loadName = intent.getStringExtra(intent.EXTRA_TEXT);
        if (intent.hasExtra("SCOUTING_FORM")) {
            m_currentForm = (ScoutingForm) intent.getSerializableExtra("SCOUTING_FORM");
        }
        m_nextButton = findViewById(R.id.nextButton);
        m_sendButton = findViewById(R.id.exitbutton);
        load = findViewById(R.id.loadButton);

        m_currentForm = new ScoutingForm();


//yes
        m_nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MatchActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
//yes
        m_sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SendMessageActivity.class);
                startActivity(intent);
            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoadActivity.class);
                startActivity(intent);
            }
       });


    }
}