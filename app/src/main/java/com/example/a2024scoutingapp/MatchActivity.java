package com.example.a2024scoutingapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a2024scoutingapp.forms.ScoutingForm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;


public class MatchActivity extends AppCompatActivity {
    private ScoutingForm m_currentForm;
    private static final String TAG = "AutoActivity";
    private static final String DIRECTORY_NAME = "Logs";
    private String m_loadName;
    private EditText m_teamNumber, m_matchNumber, m_scoutName;
    private Button red, blue, teleop, auto, exit, send, m_sendButton, load;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        m_loadName = intent.getStringExtra(intent.EXTRA_TEXT);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");
        if (m_currentForm == null) {
            m_currentForm = new ScoutingForm();
        }
        m_scoutName = findViewById(R.id.scoutName);
        m_teamNumber = findViewById(R.id.teamNumber);
        m_matchNumber = findViewById(R.id.matchNumber);
        m_sendButton = findViewById(R.id.exitbutton);
        load = findViewById(R.id.loadButton);
        auto = findViewById(R.id.auto);
        teleop = findViewById(R.id.teleop);
        send = findViewById(R.id.exitbutton);
        exit = findViewById(R.id.exitbutton2);
        m_scoutName.setText(m_currentForm.scoutName);
        m_matchNumber.setText("" + (m_currentForm.matchNumber));

        red = findViewById(R.id.red);
        blue = findViewById(R.id.blue);
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
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_currentForm.scoutName = m_scoutName.getText().toString();
                m_currentForm.teamNumber = Integer.parseInt(m_teamNumber.getText().toString());
                m_currentForm.matchNumber = Integer.parseInt(m_matchNumber.getText().toString());
                Intent intent = new Intent(MatchActivity.this, Auto.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFormToFile();
                Intent intent = new Intent(MatchActivity.this, MainActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MatchActivity.this, MainActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
        teleop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_currentForm.scoutName = m_scoutName.getText().toString();
                m_currentForm.teamNumber = Integer.parseInt(m_teamNumber.getText().toString());
                m_currentForm.matchNumber = Integer.parseInt(m_matchNumber.getText().toString());
                Intent intent = new Intent(MatchActivity.this, Teleop.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
//yes
        m_sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MatchActivity.this, SendMessageActivity.class);
                startActivity(intent);
            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MatchActivity.this, LoadActivity.class);
                startActivity(intent);
            }
        });

    }
    private void saveFormToFile () {
        File logsDir = new File(getExternalFilesDir(null), DIRECTORY_NAME);
        if (!logsDir.exists() && !logsDir.mkdirs()) {
            Log.e(TAG, "Failed to create Logs directory.");
            return;
        }

        File logFile = new File(logsDir, String.format(Locale.getDefault(),
                "match-%d-team-%d.log", m_currentForm.matchNumber, m_currentForm.teamNumber));

        try (FileWriter writer = new FileWriter(logFile)) {
            writer.write(m_currentForm.toString());
            Log.i(TAG, "Form saved successfully: " + logFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "Failed to save form", e);
        }
    }
}