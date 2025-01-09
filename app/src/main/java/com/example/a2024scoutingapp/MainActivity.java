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
    private EditText m_teamNumber, m_matchNumber, m_scoutName;
    private Button m_sendButton, m_nextButton, red, blue, load;

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
        m_scoutName = findViewById(R.id.scoutName);
        m_teamNumber = findViewById(R.id.teamNumber);
        m_matchNumber = findViewById(R.id.matchNumber);
        m_nextButton = findViewById(R.id.nextButton);
        m_sendButton = findViewById(R.id.exitbutton);
        load = findViewById(R.id.loadButton);
        m_scoutName.setText(m_currentForm.scoutName);
        m_teamNumber.setText("" + m_currentForm.teamNumber);
        m_matchNumber.setText("" + (m_currentForm.matchNumber+1));

        m_currentForm = new ScoutingForm();

        red = findViewById(R.id.red);
        blue = findViewById(R.id.blue);

//yes
        m_nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_currentForm.scoutName = m_scoutName.getText().toString();
                m_currentForm.matchNumber = Integer.parseInt(m_matchNumber.getText().toString());
                m_currentForm.teamNumber = Integer.parseInt(m_teamNumber.getText().toString());
                Intent intent = new Intent(MainActivity.this, Auto.class);
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
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_currentForm.team = Constants.Team.RED;
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_currentForm.team = Constants.Team.BLUE;
            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Load.class);
                startActivity(intent);
            }
       });


    }
}